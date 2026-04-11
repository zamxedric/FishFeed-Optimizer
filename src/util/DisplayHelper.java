package util;

import java.io.File;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;

import com.kitfox.svg.app.beans.SVGIcon;

public class DisplayHelper {
    public static JLabel parsingImg(ImageIcon icon, int x, int y, int width, int height){
        JLabel label = new JLabel(icon);
        label.setBounds(x, y, width, height);
        label.setLayout(null);
        return label; 
    }

    public static JLabel parsingSvg(String path, int x, int y, int width, int height){
        SVGIcon icon = new SVGIcon();
        icon.setSvgURI(new File(path).toURI());
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
        icon.setSvgURI(new File(path).toURI());
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

    public static JRadioButton jRadioButton(String text, ButtonGroup group, ActionListener listener, boolean selected, int x, int y, int width, int height, double percentage){
        JRadioButton radioButton = new JRadioButton(text, selected);

        ImageIcon icon = new ImageIcon("src\\display_components\\CheckedBox.png");
        Image enhancedIcon = icon.getImage();

        int widthImg = 24;
        int heightImg = 24;
        Image scaledImage = enhancedIcon.getScaledInstance(widthImg, heightImg, Image.SCALE_SMOOTH);

        ImageIcon icon2 = new ImageIcon("src\\display_components\\UncheckedBox.png");

        radioButton.setIcon(icon2);
        radioButton.setSelectedIcon(new ImageIcon(scaledImage));
        radioButton.setActionCommand(text);
        radioButton.putClientProperty("percentage_value", percentage);
        radioButton.setForeground(new Color(0xFFFFFF));
        radioButton.setFont(new Font("Poppins", Font.BOLD, 20));
        radioButton.setOpaque(false);
        radioButton.setFocusable(false);
        if(group != null){
            group.add(radioButton);
        }

        if(listener != null){
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
