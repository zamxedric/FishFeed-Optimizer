package util;

import java.net.URL;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;

import view.MainFrame;
import com.kitfox.svg.app.beans.SVGIcon;

public class DisplayHelper {

    public static JButton setupSidebar(MainFrame parent, String path, int yAxis, String pageTarget){
        JButton btn = buttonSvg(path, 43, yAxis, 301, 47);
        btn.addActionListener(e -> parent.switchPage(pageTarget));
        return btn;
    }

    public static JLabel parsingImg(String path, int x, int y, int width, int height){
        URL imgUrl = DisplayHelper.class.getResource(path);
        if (imgUrl == null) {
            System.err.println("Error: Could not find image at " + path);
            JLabel errorLabel = new JLabel("Image Missing");
            errorLabel.setBounds(x, y, width, height);
            return errorLabel;
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        label.setLayout(null);
        return label; 
    }

    public static JLabel parsingSvg(String path, int x, int y, int width, int height){
        SVGIcon icon = new SVGIcon();

        try {
            URL svgURL = DisplayHelper.class.getResource(path);
            if (svgURL == null) {
                System.err.println("Could not find SVG at path: " + path);
            } else {
                icon.setSvgURI(svgURL.toURI());
            }   

        } catch (Exception e) {
            e.printStackTrace();
        }
        icon.setPreferredSize(new Dimension(width, height));
        icon.setAntiAlias(true);

        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        label.setLayout(null);

        return label;
    }

    public static JLabel fieldLabel(JPanel parent, String desc, int fSize, int x, int y, int width, int height){
        JLabel label = new JLabel(desc);
        label.setFont(new Font("Poppins", Font.BOLD, fSize));
        label.setBounds(x, y, width, height);
        label.setForeground(new Color(0xFFFFFF));
        parent.add(label);
        return label;
    }

    public static JLabel tableLabel(String desc, int fSize, int x, int y, int width, int height){
        JLabel label = new JLabel(desc);
        label.setFont(new Font("Poppins", Font.PLAIN, fSize));
        label.setBounds(x, y, width, height);
        label.setForeground(new Color(0x000404));
        return label;
    }

    public static JButton buttonSvg(String path, int x, int y, int width, int height){
        SVGIcon icon = new SVGIcon();
        try {
            URL svgURL = DisplayHelper.class.getResource(path);
            if (svgURL == null) {
                System.err.println("Could not find SVG at path: " + path);
            } else {
                icon.setSvgURI(svgURL.toURI());
            }   

        } catch (Exception e) {
            e.printStackTrace();
        }
        icon.setPreferredSize(new Dimension(width, height));
        icon.setAntiAlias(true);

        JButton button = new JButton(icon);
        button.setBackground(new Color(0x1C2C4F));
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setOpaque(false);
        button.setBounds(x, y, width, height);
        button.setBorder(null);

        return button;
    }

    public static JTextField textField(int x, int y, int width, int height){

        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setCaretColor(new Color(0x000404));
        textField.setForeground(new Color(0x000404));
        textField.setFont(new Font("Poppins", Font.PLAIN, 18));
        textField.setOpaque(false);
        textField.setBorder(null);

        return textField;
    }

   public static JRadioButton jRadioButton(String text, ButtonGroup group, ActionListener listener, boolean selected, int x, int y, int width, int height, double percentage) {
        JRadioButton radioButton = new JRadioButton(text, selected);

        URL checkedUrl = DisplayHelper.class.getResource("/resources/images/CheckedBox.png");
        URL uncheckedUrl = DisplayHelper.class.getResource("/resources/images/UncheckedBox.png");

        if (checkedUrl != null && uncheckedUrl != null) {
            ImageIcon icon = new ImageIcon(checkedUrl);
            Image enhancedIcon = icon.getImage();
            Image scaledImage = enhancedIcon.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            
            radioButton.setSelectedIcon(new ImageIcon(scaledImage));
            radioButton.setIcon(new ImageIcon(uncheckedUrl));
        } else {
            System.err.println("Missing radio button icons!");
        }

        radioButton.setActionCommand(text);
        radioButton.putClientProperty("percentage_value", percentage);
        radioButton.setForeground(new Color(0xFFFFFF));
        radioButton.setFont(new Font("Poppins", Font.BOLD, 20));
        radioButton.setOpaque(false);
        radioButton.setFocusable(false);
        
        if (group != null) {
            group.add(radioButton);
        }
        if (listener != null) {
            radioButton.addActionListener(listener);
        }

        radioButton.setBounds(x, y, width, height);
        return radioButton;
    }

    public static <T> JComboBox<T> jComboBox(int x, int y, int width, int height){
        JComboBox<T> jComboBox = new JComboBox<>();
        jComboBox.setBounds(x, y, width, height);
        jComboBox.setOpaque(false);
        jComboBox.setForeground(new Color(0x000404));
        jComboBox.setFont(new Font("Poppins", Font.BOLD, 20));

        return jComboBox;
    }
}
