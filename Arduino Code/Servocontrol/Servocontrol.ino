// servo voor tientjes.
const int servo1 = 7;
const int servo2 = 8;

// servo voor twintigjes.
const int servo3 = 9;
const int servo4 = 10;

// servo voor vijftigjes
const int servo5 = 11;
const int servo6 = 12;

// servo middelpunt is 2445 mcs
// linksom hoger
// rechtsom lager

void setup() {
  Serial.begin(9600);
  Serial.println("Begin");

}

void loop() {
  checkIfDispense();
  delay(10);
}

void checkIfDispense(){
  int tien = 0, twintig = 0, vijftig = 0;
  while (Serial.available())
  {
    // look for the next valid integer in the incoming serial stream:
    tien    = Serial.parseInt();
    twintig = Serial.parseInt();
    vijftig = Serial.parseInt();
  }
  if (tien)
  {
    dispense(servo1, servo2, tien);
  }
  if (twintig)
  {
    dispense(servo3, servo4, twintig);
  }
  if (vijftig)
  {
    dispense(servo5, servo6, vijftig);
  }
}


void dispense(int pin1, int pin2, int aantal) {
  Serial.println("Pins: " + String(pin1) + " " + String(pin2) + " Aantal: " + String(aantal));
  for (int i = 0; i < aantal; i++)
  {
    analogWrite(pin1, 230);
    analogWrite(pin2, 20);
    delay(2000);
    analogWrite(pin1, 30);
    delay(1000);
    analogWrite(pin1, 256);
    analogWrite(pin2, 256);
    delay(50);
  }
}

/*
  for(int i =0; i<45; i++)
  {
    digitalWrite(pin, HIGH);
    digitalWrite(servo2,HIGH);
    delayMicroseconds(2000); // Approximately 10% duty cycle @ 1KHz
    digitalWrite(pin, LOW);
    digitalWrite(servo2, LOW);
    delay(22);
    digitalWrite(pin, HIGH);
    digitalWrite(servo2,HIGH);
    delayMicroseconds(2000);
    digitalWrite(pin,LOW);
    digitalWrite(servo2,LOW);
    delay(22);
  }
  */
