#include <Keypad.h>
#include <AESLib.h>
#include <AddicoreRFID.h>
#include <SPI.h>

#define uchar unsigned char
#define uint  unsigned int
#define MAX_LEN 16//Maximum length of the array

uchar serNumA[5];//4 bytes tag serial number, the first 5 bytes for the checksum byte
uchar fifobytes;
uchar fifoValue;

AddicoreRFID myRFID; // create AddicoreRFID object to control the RFID module

const byte ROWS = 4;
const byte COLS = 4;
byte colPins[COLS] = {A0, A1, A2, A3}; //connect to row pinouts
byte rowPins[ROWS] = {7, 6, 4, 3}; //connect to column pinouts

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

int uid1;
int uid2;
int uid3;
int uid4;
int pincodeEncrypted;
int pincodeLength = 0;

long randNumber;

const int chipSelectPin = 10;
const int NRSTPD = 5;

String mode = "start";
//modes: start, pincode, choice, amount, banknotes, saldoview, bon, error


Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );
//_______________________________________________________setup_______________________________________________________//
void setup() {
  Serial.begin(57600);
  SPI.begin();

  pinMode(chipSelectPin, OUTPUT);             // Set digital pin 10 as OUTPUT to connect it to the RFID /ENABLE pin
  digitalWrite(chipSelectPin, LOW);         // Activate the RFID reader
  pinMode(NRSTPD, OUTPUT);                    // Set digital pin 10 , Not Reset and Power-down
  digitalWrite(NRSTPD, HIGH);
  randomSeed(analogRead(5));

  myRFID.AddicoreRFID_Init();
  //Serial.println("Program started");
}
//_______________________________________________________main loop_______________________________________________________//
void loop() {
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

  if (!stillThere() && mode != "start") {
    reset();
  }
  delay(50);
}

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
}
//_______________________________________________________rfid_______________________________________________________//
void rfid() {
  uchar status;
  uchar str[MAX_LEN];

  str[1] = 0x4400;
  status = myRFID.AddicoreRFID_Request(PICC_REQIDL, str);
  status = myRFID.AddicoreRFID_Anticoll(str);
  if (status == MI_OK) {
    currentCardID = "";
    uid1 = str[1];
    uid2 = str[2];
    uid3 = str[3];
    uid4 = str[4];

    if (mode == "start") {
      currentCardID += uid1;
      currentCardID += uid2;
      currentCardID += uid3;
      currentCardID += uid4;
      mode = "pincode";
      Serial.println("{\"event\":\"cardreceived\",\"card\":\"" + currentCardID + "\"}");
    }
  }
  myRFID.AddicoreRFID_Halt();      //Command tag into hibernation
}

boolean stillThere() { // checks if pas is still there.
  uchar status;
  uchar str[MAX_LEN];

  str[1] = 0x4400;
  status = myRFID.AddicoreRFID_Request(PICC_REQIDL, str);
  status = myRFID.AddicoreRFID_Anticoll(str);
  for (int i = 0; i < 10; i++) {
    if (status == MI_OK) {
      myRFID.AddicoreRFID_Halt();
      String tempCardID = "";
      uid1 = str[1];
      uid2 = str[2];
      uid3 = str[3];
      uid4 = str[4];
      tempCardID += uid1;
      tempCardID += uid2;
      tempCardID += uid3;
      tempCardID += uid4;
      if (currentCardID == tempCardID) {
        return true;
      } else {
        return false;
      }
    }
  }
  myRFID.AddicoreRFID_Halt();      //Command tag into hibernation
  return false;
}
//_______________________________________________________AES_______________________________________________________//
void encryptAndSend() {

  randNumber = random(1000, 9999);
  pincodePlain = String(pincodePlain.toInt() * randNumber * 17);
  pincodePlain += (randNumber);

  Serial.println("{\"event\":\"pinsend\",\"pin\":\"" + pincodePlain + "\"");
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
  delay(3000);
}

void resetWithoutError() {
  Serial.println("{\"event\":\"resetsuccess\"}");
  mode = "start";
  pincodePlain = "";
  pincodeLength = 0;
  delay(3000);
}

void pinReset() {
  pincodePlain = "";
  pincodeLength = 0;
  pincodePlain = "";
  Serial.println("{\"event\":\"pinreset\"}");
}

