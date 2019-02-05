



import java.util.List;
import javax.swing.JFrame;
import org.apache.commons.collections4.map.MultiValueMap;
import org.math.plot.Plot2DPanel;



public class main {

    public static double[] x;
    public static double[] y;

    public static void main(String[] args) {
        String filename = "C:\\Users\\user\\Desktop\\K-means-Clustering-master\\IMSProject\\src\\main\\java\\com\\mycompany\\imsproject\\wifigdansk.csv.csv";
        int k = 3;
        double err=  0.0;
        int iterations = 100;
        CSV doc = new CSV();
        
        doc.readCSV(filename);
       // x = doc.getWifiGeoX().stream().mapToDouble(Double::doubleValue).toArray();
        //y = doc.getWifiGeoY().stream().mapToDouble(Double::doubleValue).toArray();
       // plotGraph(x, y);
        
        MultiValueMap<Integer, PointVariable> clustersList;

        Kcluster k_mean = new Kcluster();
        clustersList = k_mean.clusterData(k, doc.WifiGeoPoints(), iterations,err);
        List<PointVariable> list;
        int lstSize = 0;
        Plot2DPanel plot = new Plot2DPanel();
        
        for (int key = 0; key < k; key++) {
            list = (List) clustersList.get(key);
           // System.out.println("Key: "+key+" "+list.toString());
            lstSize = list.size();
            x=new double[lstSize];
            y=new double[lstSize];
            for (int j = 0; j < lstSize; j++) {
                x[j] =  list.get(j).gettingX();
                y[j] =  list.get(j).gettingY();
                //System.out.println("Key="+key+" {"+list.get(j).getX()+", "+list.get(j).getY()+"]");
                if(list.get(j).gettingX()==list.get(j).gettingY()){
                    System.out.println("Something is same");
                }

            }
            //double[] cx = new double[1];
            //double[] cy = new double[1];
            
           // cx[0]=k_mean.CentroidsPos[key].getX();
            //cy[0]=k_mean.CentroidsPos[key].getY();
            //plot.addScatterPlot("ClusterPOint "+key,cx , cy);
          
             plot.addScatterPlot("Cluster "+key, x, y);
        }
        JFrame frame = new JFrame("Clustered Data");
        frame.setSize(500, 500);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }

    public static void plotGraph(double[] x, double[] y) {
        Plot2DPanel plot = new Plot2DPanel();
        // add a line plot to the PlotPanel
       
        plot.addScatterPlot("WIFI Data", x, y);
         //plot.addLinePlot("WIFI Data", x, y);

        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("Gdansk Wifi Data");
        frame.setSize(500, 500);
        frame.setContentPane(plot);
        frame.setVisible(true);

    }

}
