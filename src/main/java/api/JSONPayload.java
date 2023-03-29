package api;

public class JSONPayload {
    Settings settings = new Settings();
    Message message = new Message();

    public JSONPayload() {

    }

    public Settings getSettings() {
        return this.settings;
    }

    public Message getMessage() {
        return this.message;
    }
    
    public class Message {
        private String plainText = "";
        private String encryptedText = "";
        private int messageId = 0;
        public int getMessageId() {
            return messageId;
        }

        public String getPlainText() {
            return plainText;
        }
        void setPlainText(String plainText) {
            this.plainText = plainText;
        }
        public String getEncryptedText() {
            return encryptedText;
        }
        void setEncryptedText(String encryptedText) {
            this.encryptedText = encryptedText;
        }
        void setMessageId(int messageId) {
            this.messageId = messageId;
        }
    }

    public class Settings {
        private int leftRotor = 0;
        private int middleRotor = 0;
        private int rightRotor = 0;
        private int reflector = 0;
        private String counterSettings = "AAA";

        public void setRotorsReflectorCounter(int leftRotor, int middleRotor, int rightRotor, int reflector, String counterSettings) {
            this.leftRotor = leftRotor;
            this.middleRotor = middleRotor;
            this.rightRotor = rightRotor;
            this.reflector = reflector;
            this.counterSettings = counterSettings;
        }

        int[] getRotorsAndReflector() {
            return new int[]{this.leftRotor, this.middleRotor, this.rightRotor, this.reflector};
        }

        String getCounterSettings() {
            return this.counterSettings;
        }
    }

}