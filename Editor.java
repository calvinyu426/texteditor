package editor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.*;
//import jdk.internal.cmm.SystemResourcePressureImpl;

public class Editor extends Application {
    private static  int WINDOW_WIDTH = 500;
    private static  int WINDOW_HEIGHT = 500;
    private final int horizontalMargin = 5;
    public static String fileName;
    public static String inputFilename;
    Group root;
    private makeCursor textCursor = new makeCursor();
    private int lineNumber = 0;
    private String fontName = "Verdana";
    private int height = 0;
    private static final int STARTING_FONT_SIZE = 12;
    private int fontSize = STARTING_FONT_SIZE;

    private Text tempText;

    private doublyLinkedList all_Char = new doublyLinkedList();
    private doublyLinkedList showCursor = new doublyLinkedList();
    //    private doublyLinkedList<undoRedo> undo = new doublyLinkedList<undoRedo>(null);
//    private doublyLinkedList<undoRedo> redo = new doublyLinkedList<undoRedo>(null);
    ScrollBar scrollBar = new ScrollBar();
    private String temp = "";
    private int i;



    ArrayList<doublyLinkedList.Node> lineStart = new ArrayList<>();



    private class KeyEventHandler implements EventHandler<KeyEvent> {



        /**
         * The Text to display on the screen.
         */
//        private Text tempText = new Text(0, 0, "");

        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            //textCenterX = windowWidth / 2;
            //textCenterY = windowHeight / 2;

            // Initialize some empty text and add it to root so that it will be displayed.
            //displayText = new Text(textCenterX, textCenterY, "");
            // Always set the text origin to be VPos.TOP! Setting the origin to be VPos.TOP means
            // that when the text is assigned a y-position, that position corresponds to the
            // highest position across all letters (for example, the top of a letter like "I", as
            // opposed to the top of a letter like "e"), which makes calculating positions much
            // simpler!

            showCursor.addChar(new Text(5, 0, ""));
            // All new Nodes need to be added to the root in order to be displayed.
            root.getChildren().add(textCursor.cursorMove);
            root.getChildren().add(showCursor.currentNode.item);
            textCursor.makeCursor(showCursor.currentNode.item, (int) showCursor.currentNode.item.getX(), (int) showCursor.currentNode.item.getY());
        }
        //        root.getChildren().add(textroot);
        @Override
        public void handle(KeyEvent keyEvent) {
            //System.out.println(keyEvent.getCode());
            KeyCode code = keyEvent.getCode();
            if (keyEvent.isShortcutDown()) {
                if (code == KeyCode.EQUALS) {
//                    tempText.setFont(Font.font(fontName, fontSize + 4));
                    fontSize += 4;
                    render();
                } else if (code == KeyCode.MINUS) {
                    fontSize -= 4;
                    render();
                } else if (code == KeyCode.P) {
                    if (all_Char.currentNode.item != null) {
                        System.out.println((int) (all_Char.currentNode.item.getX() + Math.round(all_Char.currentNode.item.getLayoutBounds().getWidth())) + ", " + (int) all_Char.currentNode.item.getY());
                    }
                } else if (code == KeyCode.S) {
                    Save();
                 /* Undo and Redo part*/
//                } else if(code == KeyCode.Z) {
//                    if (undo.currentPos() > 0) {
//                        undoRedo last = undo.deleteChar();
//                        if (last.isInput()) {
//                            Text t = all_Char.deleteChar();
//                            root.getChildren().remove(t);
//                        } else {
//                            all_Char.addChar(last.getContent());
//                            root.getChildren().add(last.getContent());
//                        }
//                        last.invertAction();
//                        redo.addChar(last);
//                        render();
//                    }
//                }else if(code == KeyCode.Y){
//                        if(redo.currentPos() > 0){
//                            undoRedo last = redo.deleteChar();
//                            if(last.isInput()){
//                                Text t = all_Char.deleteChar();
//                                root.getChildren().remove(t);
//                            }else {
//                                all_Char.addChar(last.getContent());
//                                root.getChildren().add(last.getContent());
//                            }
//                            last.invertAction();
//                            undo.addChar(last);
//                            render();
//                    }
//                }
                }
            }else if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.
                String characterTyped = keyEvent.getCharacter();
                if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8 && !keyEvent.isShortcutDown()) {
                    // Ignore control keys, which have non-zero length, as well as the backspace
                    // key, which is represented as a character of value = 8 on Windows.
                    Text tempText = new Text(characterTyped);
                    tempText.setTextOrigin(VPos.TOP);
                    tempText.setFont(Font.font(fontName, fontSize));
                    all_Char.addChar(tempText);
                    System.out.println(tempText);
                    i += 1;
                    render();
                    root.getChildren().add(all_Char.currentNode.item);
                }
                keyEvent.consume();
            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                doublyLinkedList.Node nextNode = all_Char.currentNode.next;
                if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    if (!all_Char.isEmpty()) {
                        Text delete = all_Char.deleteChar().item;
                        root.getChildren().remove(delete);
                    }
                    System.out.println(all_Char.size());
                    render();
                } else if (code == KeyCode.RIGHT) {
                    if (all_Char.currentNode.next.item != null) {
                        all_Char.currentNode = all_Char.currentNode.next;
                    }
                    render();
                } else if (code == KeyCode.LEFT) {
                    if (all_Char.currentNode.item != null) {
                        all_Char.currentNode = all_Char.currentNode.prev;
                    }
                    render();
                } else if (code == KeyCode.UP) {
                    if (lineNumber == 0) {
                        render();
                    } else {
                        lineNumber -= 1;
                        doublyLinkedList.Node firstChar = lineStart.get(lineNumber);
                        while (Math.abs(firstChar.item.getX() - tempText.getX()) <= 0.2 && firstChar != lineStart.get((lineNumber + 1) % lineStart.size())) {
                            firstChar = firstChar.next;
                        }
                    }
                    render();
                } else {
                    if (code == KeyCode.DOWN) {
                        nextNode = all_Char.currentNode.next;
                        while (Math.abs((int) nextNode.item.getX() - (int) all_Char.currentNode.item.getX()) <= 5) {
                            nextNode = nextNode.next;
                            all_Char.currentNode = nextNode;
                            render();
                        }
                    }
                }
                keyEvent.consume();
            }
        }
    }


    public void Save(){
        try {
            String filename = getParameters().getRaw().get(0);

            File newFile = new File(fileName);

            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for(int i = 0; i < all_Char.size() - 1; i++) {
                writer.write(all_Char.get(i).getText());
            }
            writer.close();
        }catch (IOException ioException) {
            System.out.println("Error when copying; exception was: " + ioException);
        }

    }




    private void render() {
        double total_width = horizontalMargin;
        height = 0;
        all_Char.iter = all_Char.sentinel;
        Text dummy = new Text("a");
        dummy.setFont(Font.font(fontName, fontSize));
        int fontheight = (int)dummy.getLayoutBounds().getHeight();
        doublyLinkedList.Node wordBegin = all_Char.sentinel.next;


        for (doublyLinkedList.Node i = all_Char.sentinel.next; i != all_Char.sentinel ; i = i.next) {
            Text tempText = i.item;
            tempText.setFont(Font.font(fontName, fontSize));

            if (tempText.getText().equals("\r")) {
                wordBegin = i.next;
                total_width = horizontalMargin;
                tempText.setX(total_width);
                height += fontheight;
                lineStart.add((lineNumber + 1), i);
                tempText.setY(height);
            } else if (tempText.getText().equals(" ")) {
                tempText.setX(total_width);
                tempText.setY(height);
                wordBegin = i.next;
                total_width += tempText.getLayoutBounds().getWidth();
//                    newNode = tempTextNode;
//                    changeLineNode = tempTextNode.prev;
//                    System.out.println(tempTextNode.item);
//

//                    while (newNode.next != all_Char.sentinel) {
//                        newNode = newNode.next;
//                        remainingSpace += (int) newNode.item.getLayoutBounds().getWidth();
//                        System.out.print(remainingSpace);
//                        if (remainingSpace > WINDOW_WIDTH - currentSpace) {
//                            System.out.print(remainingSpace);
//                            total_width = horizontalMargin;
//                            height += (int) Math.round(newNode.item.getLayoutBounds().getHeight());
//                            remainingSpace = 0;
//                            changeLineNode.next.next.item.setY(height);
////                                if(tempText.getX() > WINDOW_WIDTH){
////                                height += (int) Math.round(newNode.item.getLayoutBounds().getHeight());
////                                }
//                        }
//                    }
            } else{
                if (total_width + tempText.getLayoutBounds().getWidth() > WINDOW_WIDTH - horizontalMargin - scrollBar.getWidth()){
                    if(wordBegin.item.getX() == horizontalMargin) {
                        wordBegin = i;
//                            while (tempText.getText().equals(" ") && tempText.getX() > WINDOW_WIDTH) {
//                                tempTextNode = tempTextNode.next;
//                                height += fontheight;
//                                tempText.setX(horizontalMargin);
//                                tempText.setY(height);
//
                        total_width = horizontalMargin;
                        height += fontheight;
                        tempText.setX(total_width);
                        tempText.setY(height);
                        lineStart.add(i);
                        lineNumber += 1;
                        total_width += tempText.getLayoutBounds().getWidth();
                    } else {
                        i = wordBegin;
                        total_width = horizontalMargin;
                        height += fontheight;
                        wordBegin.item.setX(total_width);
                        wordBegin.item.setY(height);
                        lineStart.add(i);
                        lineNumber += 1;
                        total_width += tempText.getLayoutBounds().getWidth();

                    }
                } else {
                    tempText.setX(total_width);
                    tempText.setY(height);
                    double width = Math.round(tempText.getLayoutBounds().getWidth());
                    total_width += width;
                }


            }
        }
        if (all_Char.size() == 0) {
            textCursor.makeCursor(all_Char.currentNode.item, 5, 0);
        }
        if (all_Char.size() > 0) {
            textCursor.makeCursor(all_Char.currentNode.item, (int) all_Char.currentNode.item.getX(), (int) all_Char.currentNode.item.getY());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        root = new Group();

        Group textRoot = new Group();
        root.getChildren().add(textRoot);
        double usableScreenWidth;
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);


        EventHandler<KeyEvent> keyEventHandler =
                new KeyEventHandler(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(WINDOW_HEIGHT);
        scrollBar.setMin(0);
        scrollBar.setMax(WINDOW_HEIGHT);
        scrollBar.setLayoutX(WINDOW_WIDTH - scrollBar.getLayoutBounds().getWidth());
        root.getChildren().add(scrollBar);


        try {
            String fileName = getParameters().getRaw().get(0);
            File inputFile = new File(fileName);

            if (inputFile.exists()) {
                FileReader reader = new FileReader(inputFile);

                BufferedReader bufferedReader = new BufferedReader(reader);

                int intRead = -1;
                while ((intRead = bufferedReader.read()) != -1) {
                    char charRead = (char) intRead;

                    if (charRead  == '\r') {
                        Text newChar = new Text(""+charRead);
                        newChar.setTextOrigin(VPos.TOP);
                        root.getChildren().add(newChar);
                        all_Char.addChar(newChar);
                        intRead = bufferedReader.read();
                        if (intRead == -1 ) {
                            break;
                        } else if(intRead == '\n') {
                            continue;
                        } else {
                            newChar = new Text(""+ (char) intRead);
                            newChar.setTextOrigin(VPos.TOP);
                            root.getChildren().add(newChar);
                            all_Char.addChar(newChar);
                        }
                    } else {
                        if(all_Char != null) {
                            Text newChar = new Text(String.valueOf(charRead));
                            newChar.setTextOrigin(VPos.TOP);
                            all_Char.addChar(newChar);
                            root.getChildren().add(newChar);
                        }
                    }

                }
                bufferedReader.close();
            }
        }catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
        } catch (IOException ioException) {
            System.out.println("Error when copying; exception was: " + ioException);
        }


        render();


        /** When the scroll bar changes position, change the height of Josh. */
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                root.setLayoutY(-newValue.doubleValue());
                scrollBar.setLayoutY(newValue.doubleValue());
                render();
            }
        });

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                WINDOW_WIDTH = newScreenWidth.intValue();
                double usableScreenWidth = WINDOW_WIDTH - scrollBar.getLayoutBounds().getWidth();
                scrollBar.setLayoutX(usableScreenWidth);
                render();
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                WINDOW_HEIGHT = newScreenHeight.intValue();
                scrollBar.setPrefHeight(WINDOW_HEIGHT);

                render();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Calvin's Editor");
        textCursor.makeRectangleColorChange();
        // This is boilerplate, necessary to setup the window where things are displayed.
    }


    public class makeCursor {
        private Rectangle cursorMove;

        public makeCursor() {
            // Create a rectangle to surround the text that gets displayed.  Initialize it with a size
            // of 0, since there isn't any text yet.
            cursorMove = new Rectangle(0, 0);
        }

        protected void makeCursor(Text displayText, int Left, int upperBound) {
            // Figure out the size of the current text.
            double textHeight = Math.round(displayText.getLayoutBounds().getHeight());
            double textWidth = Math.round(displayText.getLayoutBounds().getWidth());
            cursorMove.setHeight(textHeight);
            cursorMove.setWidth(1);

            // For rectangles, the position is the upper left hand corner.
            cursorMove.setX(Left + textWidth);
            cursorMove.setY(upperBound);

            // Many of the JavaFX classes have implemented the toString() function, so that
            // they print nicely by default.
            //System.out.println("Bounding box: " + cursorMove);

            // Make sure the text appears in front of the rectangle.
            displayText.toFront();
        }


        /**
         * An EventHandler to handle changing the color of the rectangle.
         */
        private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
            private int currentColorIndex = 0;
            private Color[] boxColors =
                    {Color.BLACK, Color.WHITE};

            RectangleBlinkEventHandler() {
                // Set the color to be the first color in the list.
                changeColor();
            }

            private void changeColor() {
                cursorMove.setFill(boxColors[currentColorIndex]);
                currentColorIndex = (currentColorIndex + 1) % boxColors.length;
            }

            @Override
            public void handle(ActionEvent event) {
                changeColor();
            }
        }

        /**
         * Makes the text bounding box change color periodically.
         */
        public void makeRectangleColorChange() {
            // Create a Timeline that will call the "handle" function of RectangleBlinkEventHandler
            // every 1 second.
            final Timeline timeline = new Timeline();
            // The rectangle should continue blinking forever.
            timeline.setCycleCount(Timeline.INDEFINITE);
            RectangleBlinkEventHandler cursorChange = new RectangleBlinkEventHandler();
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        }
    }

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Not enough arguments");
            System.exit(1);
        }
        fileName = args[0];
        launch(args);

    }
}