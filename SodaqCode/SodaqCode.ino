#include <Sodaq_RN2483.h>

#define debugSerial SerialUSB
#define loraSerial Serial2

#define NIBBLE_TO_HEX_CHAR(i) ((i <= 9) ? ('0' + i) : ('A' - 10 + i))
#define HIGH_NIBBLE(i) ((i >> 4) & 0x0F)
#define LOW_NIBBLE(i) (i & 0x0F)

const char *appEui = "70B3D57ED001A5C4";
const char *appKey = "5B89DA999AA350A808BFC5DFD8E5BBD6";
const char *DevEUI2 = "0004A30B002014E2";
bool OTAA = true;

static uint8_t DevEUI[8]
{
 0x00, 0x04, 0xA3, 0x0B, 0x00, 0x20, 0x14, 0xE2 
};

const uint8_t AppEUI[8] =
{
 0x70, 0xB3, 0xD5, 0x7E, 0xD0, 0x01, 0xA5, 0xC4
};

const uint8_t AppKey[16] =
{ 0x5B, 0x89, 0xDA, 0x99, 0x9A, 0xA3, 0x50, 0xA8, 0x08, 0xBF, 0xC5, 0xDF, 0xD8, 0xE5, 0xBB, 0xD6 };

void setup()
{
  delay(1000);
  
  while ((!debugSerial) && (millis() < 10000)){
  }

  debugSerial.println("Start");

  debugSerial.begin(57600);
  loraSerial.begin(LoRaBee.getDefaultBaudRate());

  LoRaBee.setDiag(debugSerial); 
  LoRaBee.init(loraSerial, LORA_RESET);

  getHWEUI();
  debugSerial.print("LoRa HWEUI: ");
  for (uint8_t i = 0; i < sizeof(DevEUI); i++) {
    debugSerial.print((char)NIBBLE_TO_HEX_CHAR(HIGH_NIBBLE(DevEUI[i])));
    debugSerial.print((char)NIBBLE_TO_HEX_CHAR(LOW_NIBBLE(DevEUI[i])));
  }
  debugSerial.println();  
  pinMode(LED_BUILTIN, OUTPUT);
  
  setupLoRa();
}

void setupLoRa(){

    setupLoRaOTAA();

  LoRaBee.setSpreadingFactor(9);
}



void setupLoRaOTAA(){
  
  if (LoRaBee.initOTA(loraSerial, DevEUI, AppEUI, AppKey, true))
  {
    debugSerial.println("Network connection successful.");
  }
  else
  {
    debugSerial.println("Network connection failed!");
  }
}

void loop()
{
   digitalWrite(LED_BUILTIN, HIGH);
   String reading = getTemperature();
   debugSerial.println(reading);

    switch (LoRaBee.send(1, (uint8_t*)reading.c_str(), reading.length()))
    {
    case NoError:
      debugSerial.println("Successful transmission.");
      break;
    case NoResponse:
      debugSerial.println("There was no response from the device.");
      break;
    case Timeout:
      debugSerial.println("Connection timed-out. Check your serial connection to the device! Sleeping for 20sec.");
      delay(20000);
      break;
    case PayloadSizeError:
      debugSerial.println("The size of the payload is greater than allowed. Transmission failed!");
      break;
    case InternalError:
      debugSerial.println("Oh No! This shouldn't happen. Something is really wrong! The program will reset the RN module.");
      setupLoRa();
      break;
    case Busy:
      debugSerial.println("The device is busy. Sleeping for 10 extra seconds.");
      delay(10000);
      break;
    case NetworkFatalError:
      debugSerial.println("There is a non-recoverable error with the network connection. The program will reset the RN module.");
      setupLoRa();
      break;
    case NotConnected:
      debugSerial.println("The device is not connected to the network. The program will reset the RN module.");
      setupLoRa();
      break;
    case NoAcknowledgment:
      debugSerial.println("There was no acknowledgment sent back!");
      break;
    default:
      break;
    }

    digitalWrite(LED_BUILTIN, LOW);
    delay(10000); 
}

String getTemperature()
{
  //10mV per C, 0C is 500mV
  float mVolts = (float)analogRead(TEMP_SENSOR) * 3300.0 / 1023.0;
  float temp = (mVolts - 500.0) / 10.0;
  
  return String(temp);
}

static void getHWEUI()
{
  uint8_t len = LoRaBee.getHWEUI(DevEUI, sizeof(DevEUI));
}
