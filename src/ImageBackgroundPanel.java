class ImageBackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float opacity = 0.3f; // ⬅ sama kayak board kiri

    public ImageBackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)
            );

            g2d.drawImage(
                    backgroundImage,
                    0, 0,
                    getWidth(),
                    getHeight(),
                    this
            );

            g2d.dispose();
        }
    }
}