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
            String size = token.nextToken();
            if (!size.equals("B") && !size.equals("W") && !size.equals("L")){
                this.errorLabel.setText("Illegal size for assembly instruction");
            }else{
                String dx = token.nextToken();
            if (dx.charAt(0) == 'D' && dx.charAt(1) >= '0' && dx.charAt(1) <= '7') {
               String dy = token.nextToken();
               if (dy.charAt(0) == 'D' && dy.charAt(1) >= '0' && dy.charAt(1) <= '7') {
                  int sizeCode;
                  int binary;
                  if (object.equals("ADD")) {
                     binary = 0b1101 << 12;
                     sizeCode = "BWL".indexOf(size);
                     if (sizeCode < 0) {
                        this.errorLabel.setText("Illegal size for assembly instruction");
                        return;
                     }

                     binary = binary | sizeCode << 6;
                  } else if (object.equals("SUB")) {
                     binary = 0b1001 << 12;
                     sizeCode = "BWL".indexOf(size);
                     if (sizeCode < 0) {
                        this.errorLabel.setText("Illegal size for assembly instruction");
                        return;
                     }

                     binary = binary | sizeCode << 6;
                  } else if (object.equals("MULS")) {
                     binary = 49600;
                     if (!size.equals("W")) {
                        this.errorLabel.setText("Illegal size for assembly instruction");
                        return;
                     }
                  } else {
                     if (!object.equals("DIVS")) {
                        this.errorLabel.setText("Illegal operation for assembly instruction");
                        return;
                     }

                     binary = 33216;
                     if (!size.equals("W")) {
                        this.errorLabel.setText("Illegal size for assembly instruction");
                        return;
                     }
                  }

                  binary |= Integer.parseInt("" + dx.charAt(1)) << 9;
                  binary |= Integer.parseInt("" + dy.charAt(1));
                  this.binaryInstruction.setText(this.shortToBinary((short)binary));
                  this.hexInstruction.setText(this.shortToHex((short)binary));
               } else {
                  this.errorLabel.setText("Illegal destination register for assembly instruction");
               }
             } else {
               this.errorLabel.setText("Illegal source register for assembly instruction");
                }
            }
        }
    }

    void decodeBin() {
		this.assemblerInstruction.setText("");
      this.hexInstruction.setText("");
      String s = this.binaryInstruction.getText().trim();
      if (s.length() != 16) {
         this.errorLabel.setText("Binary number must be 16 bits");
      } else {
        int binary;
        try {
            binary = Integer.parseInt(s, 2);
        } catch (Exception var4) {
            this.errorLabel.setText("Illegal binary number");
            return;
        }

        this.hexInstruction.setText(this.shortToHex((short)binary));
        this.decode(binary);
      }
	}

	void decodeHex() {
	this.assemblerInstruction.setText("");
    this.binaryInstruction.setText("");
    String s = this.hexInstruction.getText().trim();
    if (s.length() != 4) {
        this.errorLabel.setText("Hex number must be 4 digits");
      } else {
         int binary;
         try {
            binary = Integer.parseInt(s, 16);
         } catch (Exception var4) {
            this.errorLabel.setText("Illegal hex number");
            return;
         }

         this.binaryInstruction.setText(this.shortToBinary((short)binary));
         this.decode(binary);
      }
	}

	void decode(int binary) {
		if ((binary & -65536) == 0 && (binary & 56) == 0) {
		   String assem = "";
		   if ((binary & '\uf000') == 53248) {
			  assem = "ADD";
		   } else if ((binary & '\uf000') == 36864) {
			  assem = "SUB";
		   } else if ((binary & '\uf1c0') == 49600) {
			  assem = "MULS.W ";
		   } else {
			  if ((binary & '\uf1c0') != 33216) {
				 this.errorLabel.setText("Illegal instruction");
				 return;
			  }
  
			  assem = "DIVS.W ";
		   }
  
		   if (!assem.equals("ADD") && !assem.equals("SUB")) {
			  if ((binary >> 6 & 7) != 7) {
				 this.errorLabel.setText("Illegal size");
				 return;
			  }
		   } else {
			  if ((binary >> 6 & 7) > 2) {
				 this.errorLabel.setText("Illegal size");
				 return;
			  }
  
			  assem = assem + "." + "BWL".charAt(binary >> 6 & 7) + " ";
		   }
  
		   assem = assem + "D" + (binary >> 9 & 7) + ",D" + (binary & 7);
		   this.assemblerInstruction.setText(assem);
		} else {
		   this.errorLabel.setText("Illegal instruction");
		}
	 }
} 