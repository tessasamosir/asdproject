class FullBackgroundPanel extends JPanel {
    private Image bg;
    private float opacity = 0.35f; // ⬅ TURUNKAN / NAIKKAN DI SINI

    public FullBackgroundPanel(String path) {
        bg = new ImageIcon(path).getImage();
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bg != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            opacity
                    )
            );
            g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            g2.dispose();
        }
    }
}