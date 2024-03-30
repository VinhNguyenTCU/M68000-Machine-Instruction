import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class M6800MachineInstruction extends JFrame implements ActionListener{
    static final long serialVersionUID = 1L;
    private JTextField assemblerInstruction;
    private JTextField binaryInstruction;
    private JTextField hexInstruction;
    private JLabel errorLabel;

    public M6800MachineInstruction(){
        setTitle("M6800");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        assemblerInstruction = new JTextField();
        assemblerInstruction.setBounds(25,24,134,28);
        getContentPane().add(assemblerInstruction);
        assemblerInstruction.setColumns(10);

        JLabel lblAssemblyLanguage = new JLabel("Assembly Language");
        lblAssemblyLanguage.setBounds(25,64,160,16);
        getContentPane().add(lblAssemblyLanguage);

        binaryInstruction = new JTextField();
        binaryInstruction.setBounds(25,115,180,28);
        getContentPane().add(binaryInstruction);
        binaryInstruction.setColumns(10);

        hexInstruction = new JTextField();
        hexInstruction.setBounds(236,115,134,28);
        getContentPane().add(hexInstruction);
        hexInstruction.setColumns(10);

        JLabel lblBinary = new JLabel("Binary Instruction");
        lblBinary.setBounds(25,155,190,16);
        getContentPane().add(lblBinary);

        JLabel lblHexEquivalent = new JLabel("Hex Instruction");
        lblHexEquivalent.setBounds(236,155,131,16);
        getContentPane().add(lblHexEquivalent);

        errorLabel = new JLabel("");
        errorLabel.setBounds(25,235,200,16);
        getContentPane().add(errorLabel);

        JButton btnEncode = new JButton("Encode");
        btnEncode.setBounds(230, 25,117,29);
        getContentPane().add(btnEncode);
        btnEncode.addActionListener(this);

        JButton btnDecode = new JButton("Decode Binary");
        btnDecode.setBounds (30,183,170,29);
        getContentPane().add(btnDecode);
        btnDecode.addActionListener(this);

        JButton btnDecodeHex = new JButton("Decode Hex");
        btnDecodeHex.setBounds(230, 183,150,29);
        getContentPane().add(btnDecodeHex);
        btnDecodeHex.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event){
        errorLabel.setText("");
        if (event.getActionCommand().equals("Encode")){
            encode();
        }else if (event.getActionCommand().equals("Decode Binary")){
            decodeBin();
        }else if (event.getActionCommand().equals("Decode Hex")){
            decodeHex();
        }
    }

    public static void main(String[] args) {
        M6800MachineInstruction window = new M6800MachineInstruction();
        window.isVisible();
    }

    String shortToHex(short x){
        String ans="";
        for (int i=0; i < 4; i++){
            int hex = x & 15;
            char hexChar = "0123456789ABCDEF".charAt(hex);
            ans = hexChar + ans;
            x = (short) (x >> 4);
        }
        return ans;
    }

    String shortToBinary(short x){
        String ans="";
        for (int i=0; i<16;i++){
            ans = (x & 1) + ans;
            x = (short) (x >> 1);
        }
        return ans;
    }

    void encode(){
        this.binaryInstruction.setText("");
        this.hexInstruction.setText("");
        String assem = this.assemblerInstruction.getText().trim().toUpperCase();
        if (!assem.startsWith("ADD") && !assem.startsWith("SUB")){

            if (!assem.startsWith("MULS") && !assem.startsWith("DIVS")){
                this.errorLabel.setText("Illegal assembly instruction");
                return;
            }

            while(true){
                if (assem.length() <= 12 || assem.charAt(7) != ' '){
                    if (assem.length() != 12 || assem.charAt(4) != '.' || assem.charAt(6) != ' ' || assem.charAt(9) != ','){
                        this.errorLabel.setText("Illegal format for assembly instruction");
                        return;
                    }
                    break;
                } 

                assem = assem.substring(0, 7) + assem.substring(8);
            }
        }else{
            while (assem.length() > 11 && assem.charAt(6) == ' '){
                assem = assem.substring(0, 6) + assem.substring(7);
            }

            if (assem.length() != 11 || assem.charAt(3) != '.' || assem.charAt(5) != ' ' || assem.charAt(8) != ','){
                this.errorLabel.setText("Illegal format for assembly instruction");
                return;
            } 
        }

        StringTokenizer token = new StringTokenizer(assem, " ,.");
        String object = token.nextToken();
        if (!object.equals("ADD") && !object.equals("SUB") && !object.equals("MULS") && !object.equals("DIVS")){
            this.errorLabel.setText("Illegal operation for assembly instruction");
        }else{
            String size
        }
    }
}