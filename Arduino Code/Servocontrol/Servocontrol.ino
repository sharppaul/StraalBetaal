const int servo1 = 3;
// servo middelpunt is 2445 mcs
// linksom hoger
// rechtsom lager

void setup() {
  Serial.begin(9600);
  for(int i = 0; i<30;i++){
    dispense(servo1);
  }
}

void loop() {

}

void dispense(int pin){
  digitalWrite(pin, HIGH);
  delayMicroseconds(2000); // Approximately 10% duty cycle @ 1KHz
  digitalWrite(pin, LOW);
  delay(22);
  digitalWrite(pin, HIGH);
  delayMicroseconds(2000);
  digitalWrite(pin,LOW);
  delay(22);
}
