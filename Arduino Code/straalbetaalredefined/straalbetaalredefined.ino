#include <Keypad.h>
#include <AESLib.h>
#include <AddicoreRFID.h>
#include <SPI.h>
String pincodePlain;
#define  uchar unsigned char
#define uint  unsigned int
int uid1;
int uid2;
int uid3;
int uid4;
//4 bytes tag serial number, the first 5 bytes for the checksum byte
uchar serNumA[5];
int pincodeLength = 0;
uchar fifobytes;
uchar fifoValue;
boolean tagReaded;
char key;

AddicoreRFID myRFID; // create AddicoreRFID object to control the RFID module
/////////////////////////////////////////////////////////////////////
//set the pins
/////////////////////////////////////////////////////////////////////
const int chipSelectPin = 10;
const int NRSTPD = 5;
//Maximum length of the array
#define MAX_LEN 16
const byte ROWS = 4;
const byte COLS = 4;
char keys[ROWS][COLS] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};
byte colPins[COLS] = {A0, A1, A2, A3}; //connect to row pinouts
byte rowPins[ROWS] = {7, 6, 4, 3}; //connect to column pinouts

Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );
//_______________________________________________________setup_______________________________________________________//
void setup() {
  Serial.begin(57600);
  SPI.begin();

  pinMode(chipSelectPin, OUTPUT);             // Set digital pin 10 as OUTPUT to connect it to the RFID /ENABLE pin
  digitalWrite(chipSelectPin, LOW);         // Activate the RFID reader
  pinMode(NRSTPD, OUTPUT);                    // Set digital pin 10 , Not Reset and Power-down
  digitalWrite(NRSTPD, HIGH);

  myRFID.AddicoreRFID_Init();
  tagReaded = false;
}
//_______________________________________________________main loop_______________________________________________________//
void loop() {
  pincodeInvoer();
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
    Serial.print("uid: ");
    uid1 = str[1];
    uid2 = str[2];
    uid3 = str[3];
    uid4 = str[4];
    Serial.print(uid1);
    Serial.print(uid2);
    Serial.print(uid3);
    Serial.print(uid4);

    //String lol(str);
    tagReaded = true;
  }

  myRFID.AddicoreRFID_Halt();      //Command tag into hibernation

}
//_______________________________________________________AES_______________________________________________________//
void encrypt() {
  char data[4];
  pincodePlain.toCharArray(data, 5);
  uint8_t key[] = {5, 9, 7, 5, 7, 5, 4, 5, 5, 6, 4, 2, 2, 9, 4, 15, 5, 5, 6, 4, 6, 5, 9, 59, 59, 5, 95, 95, 985, 798, 789, 165, 48, 15, 984, 51, 64, 894, 4, 65, 654, 4, 6};
  aes256_enc_single(key, data);
  Serial.println("");
  Serial.print("pin: ");
  Serial.println(data);
  aes256_dec_single(key, data);
  //Serial.println(data);
}
void aes256_enc_single(const uint8_t* key, void* data);
void aes256_dec_single(const uint8_t* key, void* data);
//_______________________gedeelte waar de pincode wordt ingevoerd en gechecked____________________________________//
void pincodeInvoer() {
  if (tagReaded == false) {
    rfid();
    pincodePlain = "";
  }
  else if (tagReaded == true) {
    keyPad();

    switch (key) {
      case 'A':
        wrongCharacter();
        break;
      case 'B':
        wrongCharacter();
        break;
      case 'C':
        wrongCharacter();
        break;
      case 'D':
        if (pincodeLength < 4) {
          pincodePlain = "";
          pincodeLength = 0;
          Serial.println("pincode te kort");
        }
        else if (pincodeLength > 4) {
          pincodePlain = "";
          pincodeLength = 0;
          Serial.println("pincode te lang");
        }
        else {
          encrypt();
          rfid();
          Serial.println("pincode verzonden");
          pincodePlain = "";
          pincodeLength = 0;
          tagReaded = false;//kan nog meer achter staan
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
      case '*':
        tagReaded = false;
        pincodePlain = "";
        pincodeLength = 0;
        Serial.println("Betaling afgebroken");
        delay(3000); //waits some time before the payment terminal accepts a new pas
        break;
      case '#':
        pincodePlain = "";
        pincodeLength = 0;
        Serial.println("pincode correctie");
        break;
      default:
        break;
    }
  }
}
void wrongCharacter() {
  pincodePlain = "";
  pincodeLength = 0;
  Serial.println("Verkeerde tjaracter");
}
void pincodeKeyInvoer() {
  pincodePlain += key;
  pincodeLength ++;
}
//_______________________lololol________________________________________________________________________________________________//
