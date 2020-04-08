package io.github.thar0.csdis;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Cutscene disassembler to a macro representation of each command.
 * 
 * @author Tharo
 *
 */
public class CsDis {

    public static final String LS = System.lineSeparator();

    public static void main(String[] args) throws IOException {
        switch(args.length) { 
        case 0:
            printHelp();
            break;
        case 1:
            if(args[0].equalsIgnoreCase("--text")) {
                printHelp();
            } else {
                print(parseCutscene(readFileForCutscene(args[0], false)));
            }
            break;
        case 2:
            if(args[0].equalsIgnoreCase("--text")) {
                print(parseCutscene(readFileForCutscene(args[1], true)));
            } else {
                writeCutsceneToFile(args[1], parseCutscene(readFileForCutscene(args[0], false)));
                print("Result written to file: " + args[1]);
            }
            break;
        default:
            if(args[0].equalsIgnoreCase("--text")) {
                writeCutsceneToFile(args[2], parseCutscene(readFileForCutscene(args[1], true)));
                print("Result written to file: " + args[2]);
            } else {
                writeCutsceneToFile(args[1], parseCutscene(readFileForCutscene(args[0], false)));
                print("Result written to file: " + args[1]);
            }
            break;
        }
    }

    public static void printHelp() {
        print("Usage: java -jar csdis.jar [--text] <infile> [outfile]");
        print("The infile may be supplied as a raw hex dump of the cutscene data or as");
        print("a text file formatted as a comma separated list of hex integers.");
    }

    public static int[] readFileForCutscene(String filePath, boolean text) throws IOException {
        if(text) {
            String contents = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            String[] entries = contents.trim().split(",");
            int[] array = new int[entries.length];
            IntBuffer buf = IntBuffer.wrap(array);
            for(String s : entries) {
                buf.put(Integer.parseUnsignedInt(s.trim().replaceAll("0x", ""),16));
            }
            return array;
        } else {
            return ByteBuffer.wrap(Files.readAllBytes(Paths.get(filePath))).asIntBuffer().array();
        }
    }

    public static void writeCutsceneToFile(String filePath, String cutscene) throws IOException {
        Writer fw = new FileWriter(filePath, false);
        fw.write(cutscene);
        fw.close();
    }

    /**
     * Faithful cutscene parser implementation based off matching decompilation.
     * 
     * @param cutscene
     *   Raw cutscene data as an array of 32-bit integers
     * @return
     *   A string containing the built macros, can be split on newline if an array is preferred
     */
    public static final String parseCutscene(int[] cutscene) {
        StringBuilder builder;
        int i = 0;

        // Cutscene Header

        int totalEntries = cutscene[i];
        i++;
        int cutsceneEndFrame = cutscene[i];
        i++;

        if(totalEntries < 0 || cutsceneEndFrame < 0) {
            // Cutscene will abort if the number of commands or the end frame is negative
            return null;
        }

        builder = new StringBuilder("BEGIN_CUTSCENE(" + totalEntries + ", " + cutsceneEndFrame + ")," + LS);

        // Cutscene Commands

        // totalEntries + 1 to include the End Cutscene command
        for (int k = 0; k < totalEntries + 1; k++) {
            int cmdType = cutscene[i];
            i++;

            // End Cutscene command encountered
            if (cmdType == -1) {
                return builder.toString() + "END_CUTSCENE" + LS;
            }

            int cmdEntries;
            boolean shouldContinue;
            int word1;
            int word2;
            int word3;
            int word4;

            switch (cmdType) {
            case 3:
                cmdEntries = cutscene[i];
                builder.append(String.format("MISC(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    builder.append(String.format("MISC_ENTRY(%s, %s, %s)" + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2)));
                    i += 12;
                }
                break;
            case 4:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_LIGHTING(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    builder.append(String.format("SET_LIGHTING_ENTRY(%s, %s, %s, %s)," + LS, 
                            formatHex(fourthByte(word1)), formatHex(thirdByte(word1)), firstShort(word1), 
                            secondShort(word2)));
                    i += 12;
                }
                break;
            case 86:
                cmdEntries = cutscene[i];
                builder.append(String.format("PLAY_BGM(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    builder.append(String.format("PLAY_BGM_ENTRY(%s, %s, %s, %s)," + LS, 
                            formatHex(fourthByte(word1)), formatHex(thirdByte(word1)), firstShort(word1), 
                            secondShort(word2)));
                    i += 12;
                }
                break;
            case 87:
                cmdEntries = cutscene[i];
                builder.append(String.format("STOP_BGM(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    builder.append(String.format("STOP_BGM_ENTRY(%s, %s, %s, %s)," + LS, 
                            formatHex(fourthByte(word1)), formatHex(thirdByte(word1)), firstShort(word1), 
                            secondShort(word2)));
                    i += 12;
                }
                break;
            case 124:
                cmdEntries = cutscene[i];
                builder.append(String.format("FADE_BGM(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    builder.append(String.format("FADE_BGM_ENTRY(%s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2)));
                    i += 12;
                }
                break;
            case 9:
                cmdEntries = cutscene[i];
                builder.append(String.format("CMD_09(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format("CMD_09_ENTRY(%s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(secondByte(word2)), formatHex(firstByte(word2)), 
                            formatHex(fourthByte(word3))));
                    i += 3;
                }
                break;
            case 140:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_TIME(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    builder.append(String.format("SET_TIME_ENTRY(%s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(secondByte(word2)), formatHex(firstByte(word2))));
                    i += 3;
                }
                break;
            case 10:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_0(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_0_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 15:
            case 17:
            case 18:
            case 23:
            case 34:
            case 39:
            case 46:
            case 76:
            case 85:
            case 93:
            case 105:
            case 107:
            case 110:
            case 119:
            case 123:
            case 138:
            case 139:
            case 144:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_1(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_1_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 14:
            case 16:
            case 24:
            case 35:
            case 40:
            case 48:
            case 64:
            case 68:
            case 70:
            case 78:
            case 80:
            case 94:
            case 116:
            case 118:
            case 120:
            case 125:
            case 131:
            case 141:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_2(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_2_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 25:
            case 36:
            case 41:
            case 50:
            case 67:
            case 69:
            case 72:
            case 74:
            case 81:
            case 106:
            case 117:
            case 121:
            case 126:
            case 132:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_3(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_3_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            cutscene[i+9], cutscene[i+10], cutscene[i+11]));
                    i += 12;
                }
                break;
            case 29:
            case 37:
            case 42:
            case 51:
            case 53:
            case 63:
            case 65:
            case 66:
            case 75:
            case 82:
            case 108:
            case 127:
            case 133:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_4(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_4_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 30:
            case 38:
            case 43:
            case 47:
            case 54:
            case 79:
            case 83:
            case 128:
            case 135:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_5(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_5_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 44:
            case 55:
            case 77:
            case 84:
            case 90:
            case 129:
            case 136:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_6(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_6_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 31:
            case 52:
            case 57:
            case 58:
            case 88:
            case 115:
            case 130:
            case 137:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_7(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_7_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 49:
            case 60:
            case 89:
            case 111:
            case 114:
            case 134:
            case 142:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_8(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_8_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 62:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_9(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_9_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 143:
                cmdEntries = cutscene[i];
                builder.append(String.format("SET_ACTOR_ACTION_10(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    builder.append(String.format(
                            "SET_ACTOR_ACTION_10_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(secondShort(word1)), firstShort(word1), 
                            secondShort(word2), formatHex(firstShort(word2)), 
                            formatHex(secondShort(word3)), formatHex(firstShort(word3)), 
                            cutscene[i+3], cutscene[i+4], cutscene[i+5], 
                            cutscene[i+6], cutscene[i+7], cutscene[i+8], 
                            asFloat(cutscene[i+9]), asFloat(cutscene[i+10]), asFloat(cutscene[i+11])));
                    i += 12;
                }
                break;
            case 1:
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CAMERA_POSITIONS(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 2;
                shouldContinue = true;
                while (shouldContinue) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    word4 = cutscene[i+3];
                    byte continueFlag = fourthByte(word1);
                    builder.append(String.format("CAMERA_POSITION_ENTRY(%s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(continueFlag), formatHex(thirdByte(word1)), firstShort(word1), 
                            asFloat(word2), 
                            secondShort(word3), firstShort(word3), 
                            secondShort(word4)));
                    if (continueFlag == -1) {
                        shouldContinue = false;
                    }
                    i += 4;
                }
                break;
            case 5:
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CAMERA_LINK_POSITIONS(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 2;
                shouldContinue = true;
                while (shouldContinue) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    word4 = cutscene[i+3];
                    byte continueFlag = fourthByte(word1);
                    builder.append(String.format("CAMERA_LINK_POSITION_ENTRY(%s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(continueFlag), formatHex(thirdByte(word1)), firstShort(word1), 
                            asFloat(word2), 
                            secondShort(word3), firstShort(word3), 
                            secondShort(word4)));
                    if (continueFlag == (byte)-1) {
                        shouldContinue = false;
                    }
                    i += 4;
                }
                break;
            case 2:
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CAMERA_FOCUS_POINTS(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 2;
                shouldContinue = true;
                while (shouldContinue) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    word4 = cutscene[i+3];
                    byte continueFlag = fourthByte(word1);
                    builder.append(String.format("CAMERA_FOCUS_POINT_ENTRY(%s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(continueFlag), formatHex(thirdByte(word1)), firstShort(word1), 
                            asFloat(word2), 
                            secondShort(word3), firstShort(word3), 
                            secondShort(word4)));
                    if (continueFlag == -1) {
                        shouldContinue = false;
                    }
                    i += 4;
                }
                break;
            case 6:
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CAMERA_LINK_FOCUS_POINTS(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 2;
                shouldContinue = true;
                while (shouldContinue) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    word4 = cutscene[i+3];
                    byte continueFlag = fourthByte(word1); //set by cs cmd
                    builder.append(String.format("CAMERA_LINK_FOCUS_POINT_ENTRY(%s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(continueFlag), formatHex(thirdByte(word1)), firstShort(word1), 
                            asFloat(word2), 
                            secondShort(word3), firstShort(word3), 
                            secondShort(word4)));
                    if (continueFlag == -1) {
                        shouldContinue = false;
                    }
                    i += 4;
                }
                break;
            case 7:
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CMD_07(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 6;
                break;
            case 8:
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CMD_08(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 6;
                break;
            case 1000:
                i++;
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("CS_TERMINATOR(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 2;
                break;
            case 19:
                cmdEntries = cutscene[i];
                builder.append(String.format("TEXTBOX(%s)," + LS, cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    word1 = cutscene[i];
                    word2 = cutscene[i+1];
                    word3 = cutscene[i+2];
                    if (secondShort(word1)==-1) {
                        builder.append(String.format("TEXTBOX_NO_TEXT(%s, %s)," + LS, 
                                firstShort(word1), 
                                secondShort(word2)));
                    } else if (firstShort(word2)==2) {
                        builder.append(String.format("TEXTBOX_LEARN_SONG(%s, %s, %s, %s)," + LS, 
                                formatHex(secondShort(word1)), firstShort(word1), 
                                secondShort(word2), 
                                formatHex(secondShort(word3))));
                    } else {
                        builder.append(String.format("TEXTBOX_DISPLAY_TEXT(%s, %s, %s, %s, %s, %s)," + LS, 
                                formatHex(secondShort(word1)), firstShort(word1), 
                                secondShort(word2), formatHex(firstShort(word2)), 
                                formatHex(secondShort(word3)), formatHex(firstShort(word3))));
                    }
                    i += 3;
                }
                break;
            case 45:
                i++;
                word1 = cutscene[i];
                word2 = cutscene[i+1];
                builder.append(String.format("SCENE_TRANS_FX(%s, %s, %s)," + LS, 
                        formatHex(secondShort(word1)), firstShort(word1), 
                        secondShort(word2)));
                i += 2;
                break;
            default:
                //TODO find out why it appears in valid cutscenes
                cmdEntries = cutscene[i];
                builder.append(String.format("UNK_CS_DATA(%s, %s)," + LS, formatHex(cmdType), cmdEntries));
                i++;
                for (int j = 0; j < cmdEntries; j++) {
                    builder.append(String.format("UNK_CS_DATA_ENTRY(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)," + LS, 
                            formatHex(cutscene[i]), formatHex(cutscene[i+1]), formatHex(cutscene[i+2]), 
                            formatHex(cutscene[i+3]), formatHex(cutscene[i+4]), formatHex(cutscene[i+5]), 
                            formatHex(cutscene[i+6]), formatHex(cutscene[i+7]), formatHex(cutscene[i+8]), 
                            formatHex(cutscene[i+9]), formatHex(cutscene[i+10]), formatHex(cutscene[i+11])));
                    i += 12;
                }
                break;
            }
        }
        // Return cutscene data even if an End Cutscene command was not encountered but total entry limit was reached
        return builder.toString() + LS;
    }

    public static final byte firstByte(int data) {
        return (byte)((data >> 0) & 0xFF);
    }

    public static final byte secondByte(int data) {
        return (byte)((data >> 8) & 0xFF);
    }

    public static final byte thirdByte(int data) {
        return (byte)((data >> 16) & 0xFF);
    }

    public static final byte fourthByte(int data) {
        return (byte)((data >> 24) & 0xFF);
    }

    public static final short firstShort(int data) {
        return (short)((data >> 0) & 0xFFFF);
    }

    public static final short secondShort(int data) {
        return (short)((data >> 16) & 0xFFFF);
    }

    public static final String asFloat(int data) {
        return Float.intBitsToFloat(data)+"f";
    }

    public static final String formatHex(byte b) {
        return "0x"+pad(Integer.toHexString(b),'0',2,true).toUpperCase();
    }

    public static final String formatHex(short s) {
        return "0x"+pad(Integer.toHexString(s),'0',4,true).toUpperCase();
    }

    public static final String formatHex(int i) {
        return "0x"+pad(Integer.toHexString(i),'0',8,true).toUpperCase();
    }

    public static final String pad(String s, char pad, int len, boolean front) {
        while(s.length() < len) {
            s = (front) ? pad+s : s+pad;
        }
        if(s.length() > len) {
            s = (front) ? s.substring(s.length()-len, s.length()) : s.substring(0, len);
        }
        return s;
    }

    public static final void print(String s) {
        System.out.println(s);
    }
}