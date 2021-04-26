/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";
    private static BoardTemplate template;



    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + "/" + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Board(8,8);
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            template = gson.fromJson(reader, BoardTemplate.class);
            //System.out.println("it has being called");

            result = new Board(template.width, template.height, boardname);
            for (SpaceTemplate spaceTemplate: template.spaces) {
                Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.setActions(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }
            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {}
            }
        }
        return null;
    }


    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate();
        template.width = board.width;
        template.height = board.height;

        for (int i=0; i<board.width; i++) {
            for (int j=0; j<board.height; j++) {
                Space space = board.getSpace(i,j);
                if (!space.getWalls().isEmpty() || !space.getActions().isEmpty()) {
                    SpaceTemplate spaceTemplate = new SpaceTemplate();
                    spaceTemplate.x = space.x;
                    spaceTemplate.y = space.y;
                    spaceTemplate.actions.addAll(space.getActions());
                    spaceTemplate.walls.addAll(space.getWalls());
                    template.spaces.add(spaceTemplate);
                }
            }
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        String filename =
                classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;

        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }
    }



    public static String getFileSource() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Json Files", "*.json"));
        File givenFile = fileChooser.showOpenDialog(null);
        if (givenFile == null) {
            return "";
        }
        return givenFile.getAbsolutePath();
    }




    public static Board loadBoardFromPC(String source) {
        if (source.equals("")) {
            return null;
        }

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(source));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            //return loadBoard(null);
            return null;
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

            result = new Board(template.width, template.height);
            for (SpaceTemplate spaceTemplate: template.spaces) {
                Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.setActions(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }
            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {}
            }
        }
        return null;
    }


    public static File getDirectory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("roborallyBoard");
        FileChooser.ExtensionFilter extensionFilter =
                new FileChooser.ExtensionFilter("JSON files (*.json)",
                        "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser.showSaveDialog(null);
    }


   public static void saveCurrentBoardToPC(Board board) {
       File directory = getDirectory();
        if (directory == null) {
            return;
        }

       BoardTemplate template = new BoardTemplate();
       template.width = board.width;
       template.height = board.height;

       for (int i=0; i< board.width; i++) {
           for (int j=0; j< board.height; j++) {
               Space space = board.getSpace(i,j);
               if (!space.getWalls().isEmpty() || !space.getActions().isEmpty()) {
                   SpaceTemplate spaceTemplate = new SpaceTemplate();
                   spaceTemplate.x = space.x;
                   spaceTemplate.y = space.y;
                   spaceTemplate.actions.addAll(space.getActions());
                   spaceTemplate.walls.addAll(space.getWalls());
                   template.spaces.add(spaceTemplate);
               }
           }
       }

       OutputStream outputStream = null;

       try {
           outputStream = new FileOutputStream(directory);
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
       GsonBuilder simpleBuilder = new GsonBuilder().
               registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
       Gson gson = simpleBuilder.create();

       JsonWriter writer = null;

       try {
           writer = gson.newJsonWriter(new OutputStreamWriter(outputStream));
           gson.toJson(template, BoardTemplate.class, writer);
           writer.close();

       } catch (IOException e) {
           e.printStackTrace();
       }

   }



    // method nr. 2 from someone.
//    public static void saveCurrentBoardToPC(){
//         FileChooser fileChooser = new FileChooser();
//         fileChooser.setInitialFileName("roborallyBoard");
//         FileChooser.ExtensionFilter extensionFilter =
//                 new FileChooser.ExtensionFilter("JSON files (*.json)",
//                         "*.json");
//         fileChooser.getExtensionFilters().add(extensionFilter);
//         File file = fileChooser.showSaveDialog(null);
//
//         if (file == null) {
//             return;
//         }
//         Gson gson = new Gson();
//         String json = gson.toJson(template);
//
//        try {
//            PrintWriter writer = new PrintWriter(new FileOutputStream(file, true));
//            writer.write(json);
//            writer.flush();
//            writer.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }



}
