import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SpringLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import java.awt.ScrollPane;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;

public class PropertiesGUI extends JDialog {
    private static final long serialVersionUID = -5163874795786648155L;
    private final JPanel contentPanel = new JPanel();
    private JLabel trainPathLabel;
    private JTextField trainPathField;
    private JTextField testPathField;

    
    private JPanel layerConfigsPanel;
    private JPanel inputChooserPanel;
    List<LayerConfigPanel> configPanels = new ArrayList<LayerConfigPanel>();
    List<JCheckBox> inputCheckBoxes = new ArrayList<JCheckBox>();
    private JTextField errorLimitField;
    
    /**
     * Create the dialog.
     */
    public PropertiesGUI() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Konfiguracja sieci neuronowej");
        setBounds(100, 100, 500, 350);
        setMinimumSize(new Dimension(600,400));
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{476, 0};
        gbl_contentPanel.rowHeights = new int[]{85, 0, 0, 0};
        gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        contentPanel.setLayout(gbl_contentPanel);
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            
            JPanel filePathPanel = new JPanel();
            GridBagLayout gbl_filePathPanel = new GridBagLayout();
            gbl_filePathPanel.columnWidths = new int[] {100, 300, 0};
            gbl_filePathPanel.rowHeights = new int[] {18, 18};
            gbl_filePathPanel.columnWeights = new double[]{0.0, 0.0, 0.0};
            gbl_filePathPanel.rowWeights = new double[]{0.0, 0.0};
            filePathPanel.setLayout(gbl_filePathPanel);
            {
                trainPathLabel = new JLabel("Plik treningowy:");
                trainPathLabel.setHorizontalAlignment(SwingConstants.LEFT);
                trainPathLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
                GridBagConstraints gbc_trainPathLabel = new GridBagConstraints();
                gbc_trainPathLabel.gridwidth = -1;
                gbc_trainPathLabel.weighty = 0.5;
                gbc_trainPathLabel.weightx = 0.3;
                gbc_trainPathLabel.fill = GridBagConstraints.HORIZONTAL;
                gbc_trainPathLabel.insets = new Insets(0, 0, 5, 5);
                gbc_trainPathLabel.gridx = 0;
                gbc_trainPathLabel.gridy = 0;
                filePathPanel.add(trainPathLabel, gbc_trainPathLabel);
            }
            
            trainPathField = new JTextField();
            GridBagConstraints gbc_trainPathField = new GridBagConstraints();
            gbc_trainPathField.weighty = 0.5;
            gbc_trainPathField.weightx = 0.7;
            gbc_trainPathField.fill = GridBagConstraints.HORIZONTAL;
            gbc_trainPathField.gridwidth = GridBagConstraints.RELATIVE;
            gbc_trainPathField.insets = new Insets(0, 0, 5, 5);
            gbc_trainPathField.gridx = 1;
            gbc_trainPathField.gridy = 0;
            filePathPanel.add(trainPathField, gbc_trainPathField);
            trainPathField.setColumns(10);
            
            JButton trainPathButton = new JButton("...");
            GridBagConstraints gbc_trainPathButton = new GridBagConstraints();
            gbc_trainPathButton.fill = GridBagConstraints.HORIZONTAL;
            gbc_trainPathButton.gridwidth = -1;
            gbc_trainPathButton.weighty = 0.5;
            gbc_trainPathButton.weightx = 0.1;
            gbc_trainPathButton.insets = new Insets(0, 0, 5, 0);
            gbc_trainPathButton.gridx = 2;
            gbc_trainPathButton.gridy = 0;
            filePathPanel.add(trainPathButton, gbc_trainPathButton);
            {
                JLabel testPathLabel = new JLabel("Plik testowy:");
                testPathLabel.setHorizontalAlignment(SwingConstants.LEFT);
                testPathLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
                GridBagConstraints gbc_testPathLabel = new GridBagConstraints();
                gbc_testPathLabel.gridwidth = -1;
                gbc_testPathLabel.weighty = 0.5;
                gbc_testPathLabel.weightx = 0.3;
                gbc_testPathLabel.fill = GridBagConstraints.HORIZONTAL;
                gbc_testPathLabel.insets = new Insets(0, 0, 0, 5);
                gbc_testPathLabel.gridx = 0;
                gbc_testPathLabel.gridy = 1;
                filePathPanel.add(testPathLabel, gbc_testPathLabel);
            }
            
            testPathField = new JTextField();
            GridBagConstraints gbc_testPathField = new GridBagConstraints();
            gbc_testPathField.insets = new Insets(0, 0, 0, 5);
            gbc_testPathField.weighty = 0.5;
            gbc_testPathField.weightx = 0.7;
            gbc_testPathField.fill = GridBagConstraints.HORIZONTAL;
            gbc_testPathField.gridwidth = GridBagConstraints.RELATIVE;
            gbc_testPathField.gridx = 1;
            gbc_testPathField.gridy = 1;
            filePathPanel.add(testPathField, gbc_testPathField);
            testPathField.setColumns(10);
            GridBagConstraints gbc_filePathPanel = new GridBagConstraints();
            gbc_filePathPanel.weightx = 1.0;
            gbc_filePathPanel.weighty = 0.2;
            gbc_filePathPanel.fill = GridBagConstraints.BOTH;
            gbc_filePathPanel.insets = new Insets(0, 0, 5, 0);
            gbc_filePathPanel.gridx = 0;
            gbc_filePathPanel.gridy = 0;
            contentPanel.add(filePathPanel, gbc_filePathPanel);
            
            JButton testPathButton = new JButton("...");
            GridBagConstraints gbc_testPathButton = new GridBagConstraints();
            gbc_testPathButton.fill = GridBagConstraints.HORIZONTAL;
            gbc_testPathButton.gridwidth = -1;
            gbc_testPathButton.weighty = 0.5;
            gbc_testPathButton.weightx = 0.1;
            gbc_testPathButton.gridx = 2;
            gbc_testPathButton.gridy = 1;
            filePathPanel.add(testPathButton, gbc_testPathButton);
            
            JPanel layersPanel = new JPanel();
            GridBagConstraints gbc_layersPanel = new GridBagConstraints();
            gbc_layersPanel.insets = new Insets(0, 0, 5, 0);
            gbc_layersPanel.gridwidth = -1;
            gbc_layersPanel.weightx = 1.0;
            gbc_layersPanel.fill = GridBagConstraints.BOTH;
            gbc_layersPanel.weighty = 0.8;
            gbc_layersPanel.gridx = 0;
            gbc_layersPanel.gridy = 1;
            contentPanel.add(layersPanel, gbc_layersPanel);
            GridBagLayout gbl_layersPanel = new GridBagLayout();
            gbl_layersPanel.columnWidths = new int[]{476, 0};
            gbl_layersPanel.rowHeights = new int[] {32, 0, 0};
            gbl_layersPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
            gbl_layersPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
            layersPanel.setLayout(gbl_layersPanel);
            
            JPanel layerCountPanel = new JPanel();
            GridBagConstraints gbc_layerCountPanel = new GridBagConstraints();
            gbc_layerCountPanel.gridwidth = -1;
            gbc_layerCountPanel.gridheight = -1;
            gbc_layerCountPanel.anchor = GridBagConstraints.NORTH;
            gbc_layerCountPanel.weighty = 0.1;
            gbc_layerCountPanel.weightx = 1.0;
            gbc_layerCountPanel.fill = GridBagConstraints.HORIZONTAL;
            gbc_layerCountPanel.gridx = 0;
            gbc_layerCountPanel.gridy = 0;
            layersPanel.add(layerCountPanel, gbc_layerCountPanel);
            GridBagLayout gbl_layerCountPanel = new GridBagLayout();
            gbl_layerCountPanel.columnWidths = new int[] {97, 41, 97, 96, 70, 70, 0};
            gbl_layerCountPanel.rowHeights = new int[]{20, 0};
            gbl_layerCountPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            gbl_layerCountPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
            layerCountPanel.setLayout(gbl_layerCountPanel);
            
            JLabel layerCountLabel = new JLabel("Liczba warstw: ");
            GridBagConstraints gbc_layerCountLabel = new GridBagConstraints();
            gbc_layerCountLabel.weightx = 0.5;
            gbc_layerCountLabel.gridwidth = -1;
            gbc_layerCountLabel.anchor = GridBagConstraints.WEST;
            gbc_layerCountLabel.insets = new Insets(0, 0, 0, 5);
            gbc_layerCountLabel.gridx = 0;
            gbc_layerCountLabel.gridy = 0;
            layerCountPanel.add(layerCountLabel, gbc_layerCountLabel);
            layerCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
            
            JSpinner layerCountSpinner = new JSpinner();
            
            layerCountSpinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent arg0) {
                    onLayerCountChanged(arg0);
                }
            });
            
            GridBagConstraints gbc_layerCountSpinner = new GridBagConstraints();
            gbc_layerCountSpinner.fill = GridBagConstraints.HORIZONTAL;
            gbc_layerCountSpinner.weightx = 0.5;
            gbc_layerCountSpinner.insets = new Insets(0, 0, 0, 5);
            gbc_layerCountSpinner.gridx = 1;
            gbc_layerCountSpinner.gridy = 0;
            layerCountPanel.add(layerCountSpinner, gbc_layerCountSpinner);
            layerCountSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
            
            JLabel errorLimitLabel = new JLabel("Docelowy błąd: ");
            errorLimitLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
            GridBagConstraints gbc_errorLimitLabel = new GridBagConstraints();
            gbc_errorLimitLabel.fill = GridBagConstraints.HORIZONTAL;
            gbc_errorLimitLabel.weightx = 0.5;
            gbc_errorLimitLabel.insets = new Insets(0, 0, 0, 5);
            gbc_errorLimitLabel.gridx = 2;
            gbc_errorLimitLabel.gridy = 0;
            layerCountPanel.add(errorLimitLabel, gbc_errorLimitLabel);
            
            errorLimitField = new JTextField();
            errorLimitField.setText("0.01");
            errorLimitField.setFont(new Font("Tahoma", Font.PLAIN, 14));
            GridBagConstraints gbc_errorLimitField = new GridBagConstraints();
            gbc_errorLimitField.fill = GridBagConstraints.HORIZONTAL;
            gbc_errorLimitField.weightx = 0.5;
            gbc_errorLimitField.insets = new Insets(0, 0, 0, 5);
            gbc_errorLimitField.gridx = 3;
            gbc_errorLimitField.gridy = 0;
            layerCountPanel.add(errorLimitField, gbc_errorLimitField);
            errorLimitField.setColumns(10);
            
            JLabel epochLimitLabel = new JLabel("Limit epok:");
            epochLimitLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
            GridBagConstraints gbc_epochLimitLabel = new GridBagConstraints();
            gbc_epochLimitLabel.fill = GridBagConstraints.HORIZONTAL;
            gbc_epochLimitLabel.weightx = 0.5;
            gbc_epochLimitLabel.insets = new Insets(0, 5, 0, 5);
            gbc_epochLimitLabel.gridx = 4;
            gbc_epochLimitLabel.gridy = 0;
            layerCountPanel.add(epochLimitLabel, gbc_epochLimitLabel);
            
            JSpinner epochLimitSpinner = new JSpinner();
            epochLimitSpinner.setModel(new SpinnerNumberModel(new Integer(100), new Integer(1), null, new Integer(1000)));
            GridBagConstraints gbc_epochLimitSpinner = new GridBagConstraints();
            gbc_epochLimitSpinner.insets = new Insets(0, 5, 0, 5);
            gbc_epochLimitSpinner.fill = GridBagConstraints.HORIZONTAL;
            gbc_epochLimitSpinner.weightx = 0.5;
            gbc_epochLimitSpinner.gridwidth = -1;
            gbc_epochLimitSpinner.anchor = GridBagConstraints.NORTH;
            gbc_epochLimitSpinner.gridx = 5;
            gbc_epochLimitSpinner.gridy = 0;
            layerCountPanel.add(epochLimitSpinner, gbc_epochLimitSpinner);
            
            layerConfigsPanel = new JPanel();
            layerConfigsPanel.setMinimumSize(new Dimension(200,100));
            GridBagConstraints gbc_layerConfigsPanel = new GridBagConstraints();
            gbc_layerConfigsPanel.weighty = 0.9;
            gbc_layerConfigsPanel.weightx = 1.0;
            gbc_layerConfigsPanel.fill = GridBagConstraints.BOTH;
            gbc_layerConfigsPanel.gridx = 0;
            gbc_layerConfigsPanel.gridy = 1;
            layersPanel.add(layerConfigsPanel, gbc_layerConfigsPanel);
            GridBagLayout gbl_layerConfigsPanel = new GridBagLayout();
            gbl_layerConfigsPanel.columnWidths = new int[] {0, 0, 0};
            gbl_layerConfigsPanel.rowHeights = new int[] {0, 0};
            gbl_layerConfigsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
            gbl_layerConfigsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
            layerConfigsPanel.setLayout(gbl_layerConfigsPanel);
            
            LayerConfigPanel layerConfig1 = new LayerConfigPanel();
            configPanels.add(layerConfig1);
            GridBagConstraints gbc_layerConfig1 = new GridBagConstraints();
            gbc_layerConfig1.weighty = 0.5;
            gbc_layerConfig1.weightx = 0.5;
            gbc_layerConfig1.gridx = 0;
            gbc_layerConfig1.gridy = 0;
            layerConfigsPanel.add(layerConfig1, gbc_layerConfig1);
            
            inputChooserPanel = new JPanel();
            GridBagConstraints gbc_inputChooserPanel = new GridBagConstraints();
            gbc_inputChooserPanel.anchor = GridBagConstraints.WEST;
            gbc_inputChooserPanel.gridx = 0;
            gbc_inputChooserPanel.gridy = 2;
            contentPanel.add(inputChooserPanel, gbc_inputChooserPanel);
            GridBagLayout gbl_inputChooserPanel = new GridBagLayout();
            gbl_inputChooserPanel.columnWidths = new int[] {30, 30, 30};
            gbl_inputChooserPanel.rowHeights = new int[] {30, 30, 30};
            gbl_inputChooserPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
            gbl_inputChooserPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
            inputChooserPanel.setLayout(gbl_inputChooserPanel);
            
            JCheckBox specialInputCheckBox = new JCheckBox("Wybierz wejścia");
            specialInputCheckBox.addItemListener(new ItemListener() {
                
                @Override
                public void itemStateChanged(ItemEvent arg0) {
                    onSpecialInputCheckBoxChanged(arg0);
                }
            });
            specialInputCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
            GridBagConstraints gbc_specialInputCheckBox = new GridBagConstraints();
            gbc_specialInputCheckBox.insets = new Insets(0, 0, 5, 5);
            gbc_specialInputCheckBox.anchor = GridBagConstraints.NORTHWEST;
            gbc_specialInputCheckBox.gridx = 0;
            gbc_specialInputCheckBox.gridy = 0;
            inputChooserPanel.add(specialInputCheckBox, gbc_specialInputCheckBox);
            
            
            
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Anuluj");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }
    
    private void onLayerCountChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();
        int value = (int)spinner.getValue();
        
        if (value > configPanels.size()) {
            while (value > configPanels.size()) {
                LayerConfigPanel layerConfig1 = new LayerConfigPanel();
                configPanels.add(layerConfig1);
                GridBagConstraints gbc_layerConfig1 = new GridBagConstraints();
                gbc_layerConfig1.weighty = 0.5;
                gbc_layerConfig1.weightx = 0.5;
                gbc_layerConfig1.gridx = configPanels.size();
                gbc_layerConfig1.gridy = 0;
                
                layerConfigsPanel.add(layerConfig1, gbc_layerConfig1);
            }
        } else if (value < configPanels.size()) {
            while (value < configPanels.size()) {
                LayerConfigPanel panel = configPanels.remove(configPanels.size() - 1);
                layerConfigsPanel.remove(panel);
            }
        }
        
        revalidate();
        repaint();
    }
    
    private void onSpecialInputCheckBoxChanged(ItemEvent e) {
        JCheckBox checkBox = (JCheckBox) e.getSource();

        if (checkBox.isSelected()) {
            int checkboxesCount = configPanels.get(0).getInputCount();
            int i = 0;
            while (i < checkboxesCount) {
                JCheckBox chckbxWejcie = new JCheckBox("Wejście" + Integer.toString(i));
                chckbxWejcie.setSelected(true);
                GridBagConstraints gbc_chckbxWejcie = new GridBagConstraints();
                gbc_chckbxWejcie.insets = new Insets(0, 0, 0, 5);
                gbc_chckbxWejcie.gridx = i;
                gbc_chckbxWejcie.gridy = 1;
                inputChooserPanel.add(chckbxWejcie, gbc_chckbxWejcie);
                inputCheckBoxes.add(chckbxWejcie);
                
                i++;
            }
        } else {
            while (inputCheckBoxes.size() > 0) {
                JCheckBox box = inputCheckBoxes.remove(inputCheckBoxes.size() - 1);
                inputChooserPanel.remove(box);
            }
        }
        
        revalidate();
        repaint();
    }
}
