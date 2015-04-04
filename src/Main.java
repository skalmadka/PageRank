import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        int N;
        int timeStep;
        float beta = (float) 0.8;
        float error = (float) 0.01;
        boolean needImprovement;

        if(args.length < 1)
            System.err.println("Error: Usage java -jar PageRank.jar <EdgeListFile>");

        //String inputFilePath = "./web-Google.txt";
        String inputFilePath = args[0];
        Map<Integer, Page> pageMap = new HashMap<Integer, Page>();
        Map<Integer, Double> prevStepRankMap = new HashMap<Integer, Double>();

        Long startTime = System.currentTimeMillis();

        //Read File
        try {
            loadPages(inputFilePath, pageMap);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Page Load Complete");

        //Page Rank Computation
        //Initialize Rank
        N = pageMap.size();//Number of pages in the graph
        Double initialRank = 1.0/N;
        for(Map.Entry PageEntry : pageMap.entrySet()){
            Page curPage = (Page)PageEntry.getValue();
            curPage.setRank(initialRank);
            prevStepRankMap.put(curPage.getPageId(),initialRank);
        }

        System.out.println("TimeStep: 0");

        //Improve Page Rank
        timeStep = 0;
        double S;
        do{
            timeStep++;
            needImprovement = false;
            S = 0;
            System.out.println("TimeStep:"+timeStep);

            for(Map.Entry PageEntry : pageMap.entrySet()){
                Page curPage = (Page)PageEntry.getValue();

                double newRank = 0;
                for(Page neighborPage : curPage.getAllInNeighbors()){
                    newRank += beta * prevStepRankMap.get(neighborPage.getPageId()) / neighborPage.getOutDegree();
                }

                prevStepRankMap.put(curPage.getPageId(), new Double(curPage.getRank()) );
                curPage.setRank(newRank);
                S += newRank;
            }

            for(Map.Entry PageEntry : pageMap.entrySet()){
                Page curPage = (Page)PageEntry.getValue();
                double adjustedPageRank = curPage.getRank()+ ((1- S)/N);
                curPage.setRank(adjustedPageRank);
            }

            for(Map.Entry PageEntry : pageMap.entrySet()){
                Page curPage = (Page)PageEntry.getValue();
                double curRank = curPage.getRank();
                double prevRank = prevStepRankMap.get(curPage.getPageId());
                if(Math.abs(curRank - prevRank) > error){
                    needImprovement = true;
                    break;
                }
            }
        }while(needImprovement);

        System.out.println("N:"+N+"\tTimeStep:"+timeStep);
        //displayPageRank(pageMap);
        try {
            writePageRankToFile(inputFilePath+"_pageRank.txt", pageMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();
        System.out.println("Time Spent: " + (endTime - startTime) + "ms");

    }

    private static void writePageRankToFile(String outFilePath, Map<Integer, Page> pageMap) throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath), "utf-8"));
        try {
            for (Map.Entry PageEntry : pageMap.entrySet()) {
                Page curPage = (Page) PageEntry.getValue();
                writer.write(curPage.getPageId() + "\t" + curPage.getRank() + "\t" + curPage.getInDegree() + "\t" + curPage.getOutDegree());
                writer.write("\n");
            }
        } finally {
            writer.close();
        }
    }
    private static void displayPageRank(Map<Integer, Page> pageMap) {

        for(Map.Entry PageEntry : pageMap.entrySet()){
            Page curPage = (Page)PageEntry.getValue();
            System.out.println("ID:"+ curPage.getPageId() + "\tRank:" + curPage.getRank() + "\tInDeg:"+ curPage.getInDegree() + "\tOutDeg:"+ curPage.getOutDegree());
        }
    }

    private static void loadPages( String inputFilePath,  Map<Integer, Page> pageMap) throws IOException {
        //Open Input File
        BufferedReader br = new BufferedReader(new FileReader(inputFilePath));

        //Read Input Graph into Adjacency Matrix
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split("\t");
            int id1 = Integer.parseInt(tokens[0]);
            int id2 = Integer.parseInt(tokens[1]);

            Page page1;
            Page page2;
            if(!pageMap.containsKey(id1)){
                page1 = new Page(id1);
                pageMap.put(id1, page1);
            } else {
                page1 = pageMap.get(id1);
            }
            if(!pageMap.containsKey(id2)){
                page2 = new Page(id2);
                pageMap.put(id2, page2);
            } else {
                page2 = pageMap.get(id2);
            }

//            page1.addOutNeighbor(page2);
//            page2.addInNeighbor(page1);
        }
    }
}
