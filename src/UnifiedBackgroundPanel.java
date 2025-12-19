class UnifiedBackgroundPanel extends JPanel {
    private Image bg;

    public UnifiedBackgroundPanel(String path) {
        bg = new ImageIcon(path).getImage();
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f
        ));
        g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        g2.dispose();
    }
}