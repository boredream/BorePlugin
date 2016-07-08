package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryHeader extends JPanel {

    private JLabel mType;
    private JLabel mID;
    private JLabel mName;
    private ButtonGroup mVarNameType;
    private JRadioButtonMenuItem mType1;
    private JRadioButtonMenuItem mType2;
    private JRadioButtonMenuItem mType3;

    public EntryHeader(OnTypeSelected onTypeSelected) {
        mType = new JLabel("Element");
        mType.setPreferredSize(new Dimension(100, 26));
        mType.setFont(new Font(mType.getFont().getFontName(), Font.BOLD, mType.getFont().getSize()));

        mID = new JLabel("ID");
        mID.setPreferredSize(new Dimension(160, 26));
        mID.setFont(new Font(mID.getFont().getFontName(), Font.BOLD, mID.getFont().getSize()));

        mName = new JLabel("VarName");
        mName.setPreferredSize(new Dimension(60, 26));
        mName.setFont(new Font(mName.getFont().getFontName(), Font.BOLD, mName.getFont().getSize()));

        mVarNameType = new ButtonGroup();
        mType1 = new JRadioButtonMenuItem("aa_bb_cc");
        mType1.setPreferredSize(new Dimension(70, 26));
        mType1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTypeSelected.onTypeSelected(1);
            }
        });
        mType1.setSelected(true);
        mVarNameType.add(mType1);

        mType2 = new JRadioButtonMenuItem("aaBbCc");
        mType2.setPreferredSize(new Dimension(60, 26));
        mType2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTypeSelected.onTypeSelected(2);
            }
        });
        mVarNameType.add(mType2);

        mType3 = new JRadioButtonMenuItem("mAaBbCc");
        mType3.setPreferredSize(new Dimension(60, 26));
        mType3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onTypeSelected.onTypeSelected(3);
            }
        });
        mVarNameType.add(mType3);

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(Box.createRigidArea(new Dimension(52, 0)));
        add(mType);
        add(Box.createRigidArea(new Dimension(12, 0)));
        add(mID);
        add(Box.createRigidArea(new Dimension(12, 0)));
        add(mName);
        add(Box.createRigidArea(new Dimension(6, 0)));
        add(mType1);
        add(mType2);
        add(mType3);
        add(Box.createHorizontalGlue());
    }

    public interface OnTypeSelected {
        void onTypeSelected(int type);
    }
}
