import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LayerConfigPanel extends JPanel {
    private static final long serialVersionUID = -8583829646754049266L;
    
    JSpinner inputCountSpinner;
    JComboBox activationFunctionComboBox;
    JSpinner neuronCountSpinner;
    JTextField learningRateField;
    JTextField inertiaField;
    JCheckBox biasCheckbox;
    
    LayerConfigPanel nextPanel;
    
    public LayerConfigPanel() {
        setLayout(new GridLayout(0, 2, 0, 0));
        
        JLabel activationFunctionLabel = new JLabel("Funkcja aktywacji: ");
        add(activationFunctionLabel);
        activationFunctionLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        List<String> functionTypeList = new ArrayList<String>();
        for (ActivationFunctionType.FunctionType type : ActivationFunctionType.FunctionType.values()) {
            functionTypeList.add(ActivationFunctionType.toString(type));
        }
        
        activationFunctionComboBox = new JComboBox(functionTypeList.toArray());
        add(activationFunctionComboBox);
        
        JLabel inputCountLabel = new JLabel("Liczba wejść:");
        inputCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(inputCountLabel);
        
        inputCountSpinner = new JSpinner();
        inputCountSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        add(inputCountSpinner);
        
        JLabel neuronCountLabel = new JLabel("Liczba neuronów:");
        neuronCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(neuronCountLabel);
        
        neuronCountSpinner = new JSpinner();
        neuronCountSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        neuronCountSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                onNeuronCountChanged(arg0);
            }
        });
        add(neuronCountSpinner);
        
        JLabel learningRateLabel = new JLabel("Współczynnik nauki:");
        learningRateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(learningRateLabel);
        
        learningRateField = new JTextField();
        learningRateField.setText("0.5");
        learningRateField.setHorizontalAlignment(JTextField.RIGHT);
        learningRateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        learningRateField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent arg0) {  
            }
            
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                onTextFieldChanged(arg0, learningRateField);
            }
            
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                onTextFieldChanged(arg0, learningRateField);
            }
        });
        add(learningRateField);
        
        JLabel inertiaLabel = new JLabel("Momentum:");
        inertiaLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(inertiaLabel);
        
        inertiaField = new JTextField();
        inertiaField.setText("0.6");
        inertiaField.setHorizontalAlignment(JTextField.RIGHT);
        inertiaField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        inertiaField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent arg0) {  
            }
            
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                onTextFieldChanged(arg0, inertiaField);
            }
            
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                onTextFieldChanged(arg0, inertiaField);
            }
        });
        add(inertiaField);
        
        JLabel biasLabel = new JLabel("bias:");
        biasLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(biasLabel);
        
        biasCheckbox = new JCheckBox("");
        biasCheckbox.setSelected(true);
        add(biasCheckbox);
    }
    
    public int getInputCount() {
        return (Integer) inputCountSpinner.getValue();
    }
    
    public NeuralLayerProperties getLayerProperties() {
        int inputCount = (Integer) inputCountSpinner.getValue();
        int neuronCount = (Integer) neuronCountSpinner.getValue();
        
        double learningRate = Double.parseDouble(learningRateField.getText());
        double inertia = Double.parseDouble(inertiaField.getText());
        boolean isUsingBias = biasCheckbox.isSelected();
        
        ActivationFunctionType.FunctionType type = ActivationFunctionType.FunctionType.values()[activationFunctionComboBox.getSelectedIndex()];
        ActivationFunction function = ActivationFunctionType.getFunction(type);
        
        NeuralLayerProperties properties = new NeuralLayerProperties(inputCount, neuronCount, learningRate,
                                                                     inertia, isUsingBias, function);
        
        return properties;
    }
    
    public void setNextPanel(LayerConfigPanel nextPanel) {
        this.nextPanel = nextPanel;
    }
    
    public void setInputCount(int inputCount) {
        inputCountSpinner.setValue(inputCount);
    }
    
    private void onTextFieldChanged(DocumentEvent event, JTextField field) {
        try {
            Double.parseDouble(field.getText());
        } catch (NumberFormatException exception) {
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    field.setText("");
                }
            });
        }
    }
    
    private void onNeuronCountChanged(ChangeEvent e) {
        if (nextPanel != null) {
            nextPanel.setInputCount((int)neuronCountSpinner.getValue());
        }
    }
}
