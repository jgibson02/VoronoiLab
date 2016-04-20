package homework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

/**
 * Displays a Voronoi Component and provides the user with a variety of controls
 * for altering the colorization of the diagram, as well as accompanying data and
 * visualizations related to the Voronoi diagram. The viewer is set to disable its
 * resizing ability to prevent anomalies with seeds and other components being outside the
 * viewer window.
 * @author John Gibson
 */
public class VoronoiViewer {

    private static final VoronoiComponent component = new VoronoiComponent();
    private static final JFrame frame = new JFrame("Voronoi Lab");
    private static final JCheckBox showDelaunay = new JCheckBox("Delaunay Triangulation");
    private static final JCheckBox showDistanceVisualization = new JCheckBox("Distance Visualization");
    private static final JCheckBox showCircumcircles = new JCheckBox("Show Circumcircles");
    private static final JMenuItem information = new JMenuItem("Information");
    private static int pointCount;
    private static final Timer t = new Timer(15, (ActionEvent ae) -> {
        pointCount = component.getSeedCount();
        if (pointCount >= 4) {
            showCircumcircles.setEnabled(true);
        } else {
            component.setShowDelaunayTriangles(false);
            component.setShowCircumcircles(false);
            showCircumcircles.setSelected(false);
            showCircumcircles.setEnabled(false);
        }
        if (pointCount >= 3) {
            showDelaunay.setEnabled(true);
        } else {
            showDelaunay.setSelected(false);
            showDelaunay.setEnabled(false);
        }
        if (pointCount > 0) {
            information.setEnabled(true);
            frame.setResizable(false);
            showDistanceVisualization.setEnabled(true);
        } else {
            frame.setResizable(true);
        }
    });

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(875, 500));

        t.start();

        Font boldFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
        Font plainFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        TitledBorder tBorder = new TitledBorder(new MatteBorder(3, 4, 3, 4, Color.WHITE), "Click to place points");
        tBorder.setTitleColor(Color.WHITE);
        component.setBorder(tBorder);

        JPanel distancePanel = new JPanel();
        distancePanel.setBorder(new TitledBorder(new EtchedBorder(), "Adjust distance metric"));
        JLabel distType = new JLabel("Distance Metric: ");
        distType.setFont(plainFont);
        distancePanel.add(distType);
        final JSpinner metricSpinner = new JSpinner();
        Component mySpinnerEditor = metricSpinner.getEditor();
        JFormattedTextField jftf = ((JSpinner.DefaultEditor) mySpinnerEditor).getTextField();
        jftf.setColumns(3);
        metricSpinner.setValue(2);
        metricSpinner.setSize(20, 50);
        metricSpinner.setFont(boldFont);

        distancePanel.add(metricSpinner);
        JLabel instructions = new JLabel("1 = Manhattan distance, 2 = Euclidian distance     ");
        instructions.setFont(plainFont);
        distancePanel.add(instructions);
        distancePanel.add(new JSeparator());
        final JButton euclidianButton = new JButton("Euclidian");
        final JButton manhattanButton = new JButton("Manhattan");
        euclidianButton.addActionListener((ActionEvent ae) -> {
            metricSpinner.setValue(2);
            component.setMinkowskiCoefficient(2);
            manhattanButton.setEnabled(true);
            euclidianButton.setEnabled(false);
        });
        manhattanButton.addActionListener((ActionEvent ae) -> {
            metricSpinner.setValue(1);
            component.setMinkowskiCoefficient(1);
            manhattanButton.setEnabled(false);
            euclidianButton.setEnabled(true);
        });
        metricSpinner.addChangeListener((ChangeEvent ce) -> {
            int input = 2;
            input = (int) metricSpinner.getValue();
            component.setMinkowskiCoefficient(input);
            switch (input) {
                case 1:
                    euclidianButton.setEnabled(true);
                    manhattanButton.setEnabled(false);
                    break;
                case 2:
                    euclidianButton.setEnabled(false);
                    manhattanButton.setEnabled(true);
                    break;
                default:
                    euclidianButton.setEnabled(true);
                    manhattanButton.setEnabled(true);
                    break;
            }
        });
        euclidianButton.setStyling(false);
        manhattanButton.setStyling(false);
        euclidianButton.setEnabled(false);
        distancePanel.add(euclidianButton);
        distancePanel.add(manhattanButton);

        JPanel buttonPanel = new JPanel();
        
        // opacity slider
        JPanel opSliderPanel = new JPanel();
        opSliderPanel.setLayout(new BoxLayout(opSliderPanel, BoxLayout.Y_AXIS));
        JLabel sliderLabel = new JLabel("Adjust opacity:");
        final JSlider opacitySlider = new JSlider(5, 255, component.getInitOpacity());
        opacitySlider.setMajorTickSpacing(50);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setPaintLabels(true);
        opacitySlider.addChangeListener((ChangeEvent e) -> {
            component.setOpacity(opacitySlider.getValue());
        });
        component.setOpacity(opacitySlider.getValue());
        opSliderPanel.add(sliderLabel);
        opSliderPanel.add(opacitySlider);
        
        // bottom buttons
        JButton recolor = new JButton("Recolor");
        recolor.addActionListener((ActionEvent ae) -> {
            component.randomizeColors();
        });
        JButton clearAll = new JButton("Clear All");
        clearAll.addActionListener((ActionEvent ae) -> {
            component.clearPoints();
        });
        recolor.setStyling(false);
        clearAll.setStyling(false);
        
        // checkboxes
        final JCheckBox showSeeds = new JCheckBox("Show Seeds");
        showSeeds.setSelected(true);
        showSeeds.addActionListener((ActionEvent ae) -> {
            component.setShowSeeds(showSeeds.isSelected());
        });
        showDistanceVisualization.setEnabled(false);
        showDistanceVisualization.setSelected(false);
        showDistanceVisualization.addActionListener((ActionEvent ae) -> {
            component.setShowDistanceVisualization(showDistanceVisualization.isSelected());
        });
        showDelaunay.setEnabled(false);
        showDelaunay.setSelected(false);
        showDelaunay.addActionListener((ActionEvent ae) -> {
            component.setShowDelaunayTriangles(showDelaunay.isSelected());
        });
        showCircumcircles.setEnabled(false);
        showCircumcircles.setSelected(false);
        showCircumcircles.addActionListener((ActionEvent ae) -> {
            component.setShowCircumcircles(showCircumcircles.isSelected());
        });
        
        buttonPanel.add(opSliderPanel);
        buttonPanel.add(new JSeparator());
        buttonPanel.add(recolor);
        buttonPanel.add(clearAll);
        buttonPanel.add(showSeeds);
        buttonPanel.add(showDistanceVisualization);
        buttonPanel.add(showDelaunay);
        buttonPanel.add(showCircumcircles);

        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");

        JMenuItem howToItem = new JMenuItem("How To Use");
        howToItem.addActionListener((ActionEvent ae) -> {
            String msg = "A Voronoi diagram is created from a "
                    + "set of points by shading regions for each point which "
                    + "contain all points closest to that point. There are "
                    + "multiple ways to determine the  distance to a point, "
                    + "however. <br>One method includes the standard Euclidian "
                    + "method: the length of a straight line between the two "
                    + "points in 2D space. Another is the Manhattan method: the "
                    + "sum of the vertical and horizontal change in location "
                    + "between the two points in 2D space. <br>The Minkowski "
                    + "distance formula is a more generalized method, which can "
                    + "find the distance between two points in any number of "
                    + "dimensions, enabling not only the use of the Euclidian "
                    + "and Manhattan method (2-dimensional and 1-dimensional "
                    + "respectively), but also any other higher number of "
                    + "dimensions. One such example of a higher-dimensional "
                    + "distance method is the Chebyshev distance method, which "
                    + "can be interpreted as the distance between two points in"
                    + " a number of dimensions equal to the limit as n (the "
                    + "number of dimensions) approaches infinity.<br>The number"
                    + " of dimensions used to create the Voronoi diagram in "
                    + "this program can be selected using the text field at the"
                    + " top of the window.";
            JOptionPane.showMessageDialog(null, "<html><body>"
                    + "<p style='width: 350px;'>" + msg + "</p></body></html>");
        });

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener((ActionEvent e) -> {
            JFrame aboutFrame = new JFrame("About");
            JPanel aboutPanel = new JPanel();
            JLabel aboutMsg = new JLabel("<html><div style='text-align: center; "
                    + "vertical-align: center'>Written by John Gibson</html>");
            aboutMsg.setVerticalTextPosition(JLabel.CENTER);
            aboutPanel.add(aboutMsg, BorderLayout.CENTER);
            aboutFrame.add(aboutPanel);
            aboutFrame.setLocationRelativeTo(null);
            aboutFrame.setMinimumSize(new Dimension(250, 100));
            aboutFrame.pack();
            aboutFrame.setVisible(true);
        });

        information.setEnabled(false);
        information.addActionListener((ActionEvent ae) -> {
            JDialog infoFrame = new JDialog(frame, "Information", true);
            infoFrame.setLocationRelativeTo(frame);
            ArrayList<Seed> seedList = component.getSeedList();
            final JList pointList = new JList(seedList.toArray());
            pointList.addListSelectionListener((ListSelectionEvent lse) -> {
                component.setCursorSeed((Seed) pointList.getSelectedValue());
            });
            JScrollPane pointScrollPane = new JScrollPane(pointList);
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBorder(new EmptyBorder(5, 7, 5, 7));
            DecimalFormat dFormat = new DecimalFormat("0.##");
            JLabel minDistanceLabel;
            JLabel maxDistanceLabel;
            if (component.getSeedCount() == 1) {
                minDistanceLabel = new JLabel("Shortest distance between two seeds: 0.00");
                maxDistanceLabel = new JLabel("Greatest distance between two seeds: 0.00");
            } else {
                minDistanceLabel = new JLabel("Shortest distance between two "
                + "seeds: " + dFormat.format(component.getMinimumDistance()) + " pixels.");
                maxDistanceLabel = new JLabel("Greatest distance between two "
                    + "seeds: " + dFormat.format(component.getMaximumDistance()) + " pixels.");
            }
            JLabel numSeeds = new JLabel("Number of seeds: " + component.getSeedCount());
            infoPanel.add(minDistanceLabel);
            infoPanel.add(maxDistanceLabel);
            infoPanel.add(numSeeds);
            infoFrame.add(pointScrollPane, BorderLayout.WEST);
            infoFrame.add(infoPanel, BorderLayout.EAST);
            infoFrame.pack();
            infoFrame.setVisible(true);
        });

        helpMenu.add(howToItem);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        menuBar.add(information);

        frame.setJMenuBar(menuBar);
        frame.add(distancePanel, BorderLayout.NORTH);
        frame.add(component, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setIconImage(new ImageIcon("Icon.png").getImage());
        frame.setVisible(true);
    }

}