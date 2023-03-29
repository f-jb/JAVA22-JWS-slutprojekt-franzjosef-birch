package enigma;

import org.junit.Test;


class EngineTest {

    final private String encodedMessage = "NRPTVLSCRMXYTGSPRFLTC";

    @Test
    void encode() {
        Engine encodeEngine = new Engine(1, 2, 3, 1, "AAA");
        String messageToEncode = "This is an enigma machine";
        String testEncodedMessage = encodeEngine.encode(messageToEncode);

    }

    @Test
    void decode() {
        Engine decodeEngine = new Engine(1, 2, 3, 1, "AAA");
        String testDecodedMessage = decodeEngine.encode(encodedMessage);
        String decodedMessage = "THISISANENIGMAMACHINE";

    }
}