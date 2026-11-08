package analysis;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Gera o grafico do benchmark (PNG) a partir do CSV produzido por Benchmark.
 * Somente Java 2D (JDK padrao, modo headless) - nao precisa de Python nem bibliotecas.
 * Uso: java ChartPng <entrada.csv> <saida.png>
 */
public class ChartPng {
    static final int W = 1080, H = 380, SCALE = 2;
    static final Color BLUE = Color.decode("#1f6feb"), ORANGE = Color.decode("#d9480f");

    record Series(String name, double[][] pts, Color color) { }

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        List<String[]> rows = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(args[0]));
        String[] header = lines.get(0).split(",");
        for (int i = 1; i < lines.size(); i++) rows.add(lines.get(i).split(","));
        int cSeries = idx(header, "series"), cN = idx(header, "n"), cRatio = idx(header, "ratio"),
            cHit = idx(header, "hit_rate_pct"), cGap = idx(header, "mean_gap_pct");

        double[][] hitN = pts(rows, "A_vary_n", cSeries, cN, cHit);
        double[][] gapN = pts(rows, "A_vary_n", cSeries, cN, cGap);
        double[][] hit20 = pts(rows, "B_vary_ratio_n20", cSeries, cRatio, cHit);
        double[][] hit50 = pts(rows, "B_vary_ratio_n50", cSeries, cRatio, cHit);

        BufferedImage img = new BufferedImage(W * SCALE, H * SCALE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.scale(SCALE, SCALE);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, W, H);
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(Color.decode("#222222"));
        centered(g, "Guloso 0/1 vs PD (oráculo) — instâncias aleatórias por ponto, semente fixa", 540, 22);

        double[] nTicks = {5, 10, 20, 50, 100, 200};
        panel(g, 60, "Acerto exato do guloso × nº de itens (W = 50%)", "n (escala log)",
              "instâncias com valor ótimo (%)", List.of(new Series("guloso 0/1", hitN, BLUE)),
              100, true, nTicks, "%.0f");
        panel(g, 420, "Perda média de valor × nº de itens", "n (escala log)", "gap médio vs ótimo (%)",
              List.of(new Series("gap médio", gapN, ORANGE)), 2, true, nTicks, "%.1f");
        panel(g, 770, "Acerto exato × razão W/Σpesos", "capacidade / peso total",
              "instâncias com valor ótimo (%)",
              List.of(new Series("n=20", hit20, BLUE), new Series("n=50", hit50, ORANGE)),
              100, false, new double[]{0.1, 0.25, 0.5, 0.75, 0.9}, "%.0f");
        g.dispose();
        File out = new File(args[1]);
        if (out.getParentFile() != null) out.getParentFile().mkdirs();
        ImageIO.write(img, "png", out);
        System.out.println("ok -> " + out);
    }

    static int idx(String[] header, String name) {
        for (int i = 0; i < header.length; i++) if (header[i].equals(name)) return i;
        throw new IllegalArgumentException("coluna ausente: " + name);
    }

    static double[][] pts(List<String[]> rows, String series, int cSeries, int cX, int cY) {
        List<double[]> list = new ArrayList<>();
        for (String[] r : rows) {
            if (r[cSeries].equals(series)) list.add(new double[]{Double.parseDouble(r[cX]), Double.parseDouble(r[cY])});
        }
        return list.toArray(new double[0][]);
    }

    static void centered(Graphics2D g, String s, double x, double y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (float) (x - fm.stringWidth(s) / 2.0), (float) y);
    }

    static void panel(Graphics2D g, int x0, String title, String xlabel, String ylabel, List<Series> series,
                      double ymax, boolean xlog, double[] xticks, String yfmt) {
        final int pw = 290, ph = 250, top = 60;
        Color grid = Color.decode("#e5e5e5"), text = Color.decode("#555555");
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(Color.decode("#222222"));
        centered(g, title, x0 + pw / 2.0, top - 14);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int i = 0; i <= 5; i++) {
            double y = top + ph - ph * i / 5.0;
            g.setColor(grid);
            g.draw(new Line2D.Double(x0, y, x0 + pw, y));
            g.setColor(text);
            String lbl = String.format(yfmt, ymax * i / 5.0);
            g.drawString(lbl, (float) (x0 - 6 - g.getFontMetrics().stringWidth(lbl)), (float) (y + 4));
        }
        g.setColor(Color.decode("#888888"));
        g.draw(new Line2D.Double(x0, top + ph, x0 + pw, top + ph));
        double lo = xticks[0], hi = xticks[xticks.length - 1];
        for (double t : xticks) {
            g.setColor(text);
            centered(g, String.format("%s", t == Math.rint(t) ? String.valueOf((long) t) : String.valueOf(t)),
                     px(x0, pw, t, lo, hi, xlog), top + ph + 16);
        }
        g.setColor(Color.decode("#333333"));
        centered(g, xlabel, x0 + pw / 2.0, top + ph + 36);
        AffineTransform old = g.getTransform();
        g.rotate(-Math.PI / 2, x0 - 38, top + ph / 2.0);
        centered(g, ylabel, x0 - 38, top + ph / 2.0);
        g.setTransform(old);

        for (Series s : series) {
            g.setColor(s.color());
            g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 1; i < s.pts().length; i++) {
                g.draw(new Line2D.Double(px(x0, pw, s.pts()[i - 1][0], lo, hi, xlog), top + ph - ph * s.pts()[i - 1][1] / ymax,
                                         px(x0, pw, s.pts()[i][0], lo, hi, xlog), top + ph - ph * s.pts()[i][1] / ymax));
            }
            for (double[] p : s.pts()) {
                double cx = px(x0, pw, p[0], lo, hi, xlog), cy = top + ph - ph * p[1] / ymax;
                g.fill(new Ellipse2D.Double(cx - 3.4, cy - 3.4, 6.8, 6.8));
            }
        }
        g.setStroke(new BasicStroke(1f));
        int lx = x0, ly = top + ph + 58;
        for (Series s : series) {
            g.setColor(s.color());
            g.setStroke(new BasicStroke(2.2f));
            g.draw(new Line2D.Double(lx, ly, lx + 16, ly));
            g.setColor(Color.decode("#333333"));
            g.drawString(s.name(), lx + 21, ly + 4);
            lx += 120;
        }
        g.setStroke(new BasicStroke(1f));
    }

    static double px(int x0, int pw, double v, double lo, double hi, boolean log) {
        if (log) return x0 + pw * (Math.log(v) - Math.log(lo)) / (Math.log(hi) - Math.log(lo));
        return x0 + pw * (v - lo) / (hi - lo);
    }
}
