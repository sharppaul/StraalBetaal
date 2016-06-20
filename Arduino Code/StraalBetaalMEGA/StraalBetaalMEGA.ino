#include <Arduino.h>
#include <MFRC522.h>
#include <Keypad.h>
#include <SPI.h>

#define RST_PIN   9     // Configurable, see typical pin layout above
#define SS_PIN    10    // Configurable, see typical pin layout above

MFRC522 mfrc522(SS_PIN, RST_PIN); // Instance of the class
MFRC522::MIFARE_Key rfidkey;
byte nuidPICC[3];// Init array that will store new NUID

const byte ROWS = 4;
const byte COLS = 4;
byte colPins[COLS] = {26, 27, 28, 29}; //connect to row pinouts
byte rowPins[ROWS] = {22, 23, 24, 25}; //connect to column pinouts

char key;
char keys[ROWS][COLS] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

String pincodePlain;
String bedrag;
String currentCardID = "none";

int pincodeEncrypted;
int pincodeLength = 0;
int red = 40;
int green = 41;
int ledOut = 38;

const int servoGround = 3;
// servo voor tientjes.
const int servo1 = 4;
const int servo2 = 5;

// servo voor twintigjes.
const int servo3 = 6;
const int servo4 = 7; //already used by Addicore...

// servo voor vijftigjes
const int servo5 = 8;
const int servo6 = 9;

// servo middelpunt is 2445 mcs
// linksom hoger
// rechtsom lager

long randNumber;

boolean led = false;

String mode = "start";
//modes: start, pincode, choice, amount, banknotes, saldoview, bon, error


Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );
//_______________________________________________________setup_______________________________________________________//
void setup() {
  Serial.begin(57600);
  while (!Serial);

  mfrc522.PCD_Init(); // Init MFRC522
  SPI.begin();


  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  pinMode(ledOut, OUTPUT);
  pinMode(servoGround, OUTPUT);

  digitalWrite(servoGround, LOW);
  digitalWrite(ledOut, HIGH);
  digitalWrite(red, HIGH);
  digitalWrite(green, LOW);

  for (byte i = 0; i < 6; i++) {
    rfidkey.keyByte[i] = 0xFF;
  }
}
//_______________________________________________________main loop_______________________________________________________//
void loop() {
  delay(100);

  if (mode == "start") {
    language();
    rfid();
  } else if (mode == "pincode") {
    pincodeInvoer();
  } else if (mode == "choice") {
    keuzeMenu();
  } else if (mode == "amount") {
    pinnen();
  } else if (mode == "banknotes") {
    bankNotes();
  } else if (mode == "saldoview") {
    saldoBekijken();
  } else if (mode == "donate") {
    donate();
  } else if (mode == "bon") {
    bon();
  } else if (mode == "done") {
    reset();
  }

  delay(100);

  if ( mode != "start") {
    if (!stillThere()) {
      digitalWrite(red, HIGH);
      digitalWrite(green, LOW);
      reset();
    }
  }

  checkIfDispense();
}

//_______________________________________________________change lnguage_______________________________________________________//
void language() {
  keyPad();
  switch (key) {
    case 'A':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"EN\"}"
      );
      break;
    case 'B':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"GER\"}"
      );
      break;
    case 'C':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"NL\"}"
      );
      break;
    default:
      break;
  }
}
//_______________________________________________________keyPad_______________________________________________________//
void keyPad() {
  key = keypad.getKey();
  // if(key != NO_KEY)
  //   Serial.println(key);
}
//_______________________________________________________rfid_______________________________________________________//
void rfid() {
  if ( ! mfrc522.PICC_IsNewCardPresent())
    return;

  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial())
    return;
  // In this sample we use the second sector,
  // that is: sector #1, covering block #4 up to and including block #7
  byte sector         = 1;
  byte blockAddr      = 4;
  byte trailerBlock   = 7;
  MFRC522::StatusCode status;
  byte buffer[18];
  byte size = sizeof(buffer);

  // Authenticate using key A
  status = (MFRC522::StatusCode) mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &rfidkey, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("PCD_Authenticate() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
    return;
  }
  // read data
  status = (MFRC522::StatusCode) mfrc522.MIFARE_Read(blockAddr, buffer, &size);
  if (status != MFRC522::STATUS_OK) {
    Serial.print(F("MIFARE_Read() failed: "));
    Serial.println(mfrc522.GetStatusCodeName(status));
  }
  //Serial.print(F("Data in block ")); Serial.print(blockAddr); Serial.println(F(":"));
  //dump_byte_array(buffer, 16); Serial.println();
  //Serial.println();

  currentCardID =   (String(mfrc522.uid.uidByte[0], HEX));
  currentCardID +=  (String(mfrc522.uid.uidByte[1], HEX));
  currentCardID +=  (String(mfrc522.uid.uidByte[2], HEX));
  currentCardID +=  (String(mfrc522.uid.uidByte[3], HEX));
  Serial.println("{\"event\":\"cardreceived\",\"card\":\"" + currentCardID + "\",\"bankid\":\"" + String(buffer[0]) + "\"}");
  mode = "pincode";
  digitalWrite(red, LOW);
  digitalWrite(green, HIGH);

  // Halt PICC
  mfrc522.PICC_HaltA();
  // Stop encryption on PCD
  mfrc522.PCD_StopCrypto1();
}

boolean stillThere() { // checks if pas is still there.
  byte buffer[18];
  byte size = sizeof(buffer);
  mfrc522.PICC_WakeupA(buffer, &size);
  boolean result = mfrc522.PICC_ReadCardSerial();
  mfrc522.PICC_HaltA();
  return result;
}
//_______________________________________________________Encryptie_______________________________________________________//
void encryptAndSend() {

  randNumber = random(1000, 9999);
  pincodePlain = String(pincodePlain.toInt() * randNumber * 17);
  pincodePlain += (randNumber);

  Serial.println("{\"event\":\"pinsend\",\"pin\":\"" + pincodePlain + "\"}");
  mode = "choice";
}
//_______________________gedeelte waar de pincode wordt ingevoerd en gechecked____________________________________//
void pincodeInvoer() {
  if (pincodeLength < 1)
    pincodePlain = "";

  keyPad();

  switch (key) {
    case 'D':
      if (pincodeLength < 4) {
        pincodePlain = "";
        pincodeLength = 0;
        Serial.println(
          "{\"event\":\"error\",\"error\":\"Pincode te kort.\"}"
        );
      }
      else if (pincodeLength > 4) {
        pincodePlain = "";
        pincodeLength = 0;
        Serial.println(
          "{\"event\":\"error\",\"error\":\"Pincode te lang.\"}"
        );
      }
      else {
        encryptAndSend();
      }
      break;
    case '1':
      pincodeKeyInvoer();
      break;
    case '2':
      pincodeKeyInvoer();
      break;
    case '3':
      pincodeKeyInvoer();
      break;
    case '4':
      pincodeKeyInvoer();
      break;
    case '5':
      pincodeKeyInvoer();
      break;
    case '6':
      pincodeKeyInvoer();
      break;
    case '7':
      pincodeKeyInvoer();
      break;
    case '8':
      pincodeKeyInvoer();
      break;
    case '9':
      pincodeKeyInvoer();
      break;
    case '0':
      pincodeKeyInvoer();
      break;
    case 'A':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"EN\"}"
      );
      break;
    case 'B':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"GER\"}"
      );
      break;
    case 'C':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"NL\"}"
      );
      break;
    case '*':
      reset();
      delay(3000);
      break;
    case '#':
      pinReset();
      break;
    default:
      break;
  }
}

void pincodeKeyInvoer() {
  pincodePlain += key;
  pincodeLength ++;
  Serial.println("{\"event\":\"pindot\"}");
}
//________________________________________________keuzemenu_______________________________________________________________________//
void keuzeMenu() {
  keyPad();
  switch (key) {
    case 'A': //SnelPinnen
      mode = "bon";
      Serial.println(
        "{\"event\":\"choice\",\"option\":\"a\"}"
      );
      break;
    case 'B':
      mode = "saldoview";
      Serial.println(
        "{\"event\":\"choice\",\"option\":\"b\"}"
      );
      break;
    case 'C':
      mode = "amount";
      Serial.println(
        "{\"event\":\"choice\",\"option\":\"c\"}"
      );
      break;
    case '1':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"EN\"}"
      );
      break;
    case '2':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"GER\"}"
      );
      break;
    case '3':
      Serial.println(
        "{\"event\":\"language\",\"option\":\"NL\"}"
      );
      break;
    case '*':
      reset();
      break;
    default:
      break;
  }
}
//_______________________________________________________saldo bekijken________________________________________________________________//
void saldoBekijken() {
  keyPad();
  switch (key) {
    case 'D':
      //resetWithoutError();
      mode = "choice";
      Serial.println("{\"event\":\"backtomenu\"}");
      break;
    case '*':
      resetWithoutError();//stoppen
      break;
    default:
      break;
  }
}
//_______________________________________________________________pinnen________________________________________________________________//
void pinnen() {
  keyPad();
  switch (key) {
    case 'A':
      Serial.println("{\"event\":\"amountkey\",\"key\":\"a\"}");
      mode = "banknotes";
      break;
    case 'B':
      Serial.println("{\"event\":\"amountkey\",\"key\":\"b\"}");
      mode = "banknotes";
      break;
    case 'C':
      Serial.println("{\"event\":\"amountkey\",\"key\":\"c\"}");
      mode = "banknotes";
      break;
    case 'D':
      mode = "banknotes";
      Serial.println("{\"event\":\"amountdone\"}");
      bedrag = "";
      break;
    case '1':
      bedragInvullen();
      break;
    case '2':
      bedragInvullen();
      break;
    case '3':
      bedragInvullen();
      break;
    case '4':
      bedragInvullen();
      break;
    case '5':
      bedragInvullen();
      break;
    case '6':
      bedragInvullen();
      break;
    case '7':
      bedragInvullen();
      break;
    case '8':
      bedragInvullen();
      break;
    case '9':
      bedragInvullen();
      break;
    case '0':
      bedragInvullen();
      break;
    case '*':
      reset();
      break;
    case '#':
      bedrag = "";
      Serial.println("{\"event\":\"amountreset\"}");
      break;
    default:
      break;
  }
}

void bedragInvullen() {
  bedrag += key;
  Serial.println("{\"event\":\"amountkey\" , \"key\" :\" " + String(key) + "\" } ");
}
//_______________________________________________________________banknotes________________________________________________________________//
void bankNotes() {
  keyPad();
  switch (key) {
    case 'A':
      Serial.println(
        "{\"event\":\"billchoice\",\"option\":\"a\"}"
      );
      mode = "donate";
      break;
    case 'B':
      Serial.println(
        "{\"event\":\"billchoice\",\"option\":\"b\"}"
      );
      mode = "donate";
      break;
    case 'C':
      Serial.println(
        "{\"event\":\"billchoice\",\"option\":\"c\"}"
      );
      mode = "donate";
      break;
    case '*':
      reset();
      break;
    default:
      break;
  }
}
//_______________________________________________________________donate________________________________________________________________//
void donate() {
  keyPad();
  switch (key) {
    case 'D':
      Serial.println(
        "{\"event\":\"donatechoice\",\"option\":\"true\"}"
      );
      mode = "bon";
      break;
    case '#':
      Serial.println(
        "{\"event\":\"donatechoice\",\"option\":\"false\"}"
      );
      mode = "bon";
      break;
    default:
      break;
  }
}//_______________________________________________________________bon________________________________________________________________//
void bon() {
  keyPad();
  switch (key) {
    case 'D':
      Serial.println(
        "{\"event\":\"bonchoice\",\"option\":\"true\"}"
      );
      mode = "bon";
      break;
    case '#':
      Serial.println(
        "{\"event\":\"bonchoice\",\"option\":\"false\"}"
      );
      mode = "bon";
      break;
    default:
      break;
  }
}
//_______________________________________________________________reset________________________________________________________________//
void reset() {
  Serial.println("{\"event\":\"reset\"}");
  mode = "start";
  pincodePlain = "";
  pincodeLength = 0;
  delay(1500);
}

void resetWithoutError() {
  Serial.println("{\"event\":\"resetsuccess\"}");
  mode = "start";
  pincodePlain = "";
  pincodeLength = 0;
  delay(1500);
}

void pinReset() {
  pincodePlain = "";
  pincodeLength = 0;
  pincodePlain = "";
  Serial.println("{\"event\":\"pinreset\"}");
}

void printHex(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}
void dump_byte_array(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i], HEX);
  }
}

//_______________________________________________________________dispenser________________________________________________________________//
void checkIfDispense() {
  int tien = 0, twintig = 0, vijftig = 0;
  while (Serial.available())
  {
    // look for the next valid integer in the incoming serial stream:
    tien    = Serial.parseInt();
    twintig = Serial.parseInt();
    vijftig = Serial.parseInt();
  }
  if (tien) {
    dispense(servo1, servo2, tien);
  }
  if (twintig) {
    dispense(servo3, servo4, twintig);
  }
  if (vijftig) {
    dispense(servo5, servo6, vijftig);
  }
}

void dispense(int pin1, int pin2, int aantal) {
  //Serial.println("Pins: " + String(pin1) + " " + String(pin2) + " Aantal: " + String(aantal));
  for (int i = 0; i < aantal; i++)
  {
    analogWrite(pin1, 230);
    analogWrite(pin2, 20);
    delay(2000);
    analogWrite(pin1, 30);
    delay(1200);
    analogWrite(pin1, 256);
    analogWrite(pin2, 256);
    delay(50);
  }
}

