package analysis;

import domain.classical.greedy.KnapsackItem;
import runner.KnapsackInstances;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Gera as figuras do relatório de Divisão e Conquista em PNG (Java 2D, sem dependências):
 *   arvore-recursao-pequena.png  árvore de recursão real da instância pequena (mesma lógica e poda de
 *                                KnapsackClassicDivideAndConquerProblem);
 *   fluxograma-dc-mochila.png    fluxograma do D&C aplicado à mochila 0/1.
 * Uso: java analysis.DcFigures <pasta-de-saida>
 */
public final class DcFigures {
    private static final Font TITLE = new Font("SansSerif", Font.BOLD, 22);
    private static final Font BOLD = new Font("SansSerif", Font.BOLD, 15);
    private static final Font PLAIN = new Font("SansSerif", Font.PLAIN, 14);
    private static final Color INK = new Color(0x22, 0x22, 0x22);
    private static final Color DIVIDE = new Color(0xDC, 0xE9, 0xF7);
    private static final Color DIVIDE_LINE = new Color(0x2F, 0x5D, 0x9A);
    private static final Color BEST = new Color(0xD8, 0xF0, 0xD8);
    private static final Color BEST_LINE = new Color(0x2E, 0x7D, 0x32);
    private static final Color PRUNE = new Color(0xF3, 0xF3, 0xF3);
    private static final Color PRUNE_LINE = new Color(0x88, 0x88, 0x88);
    private static final Color BASE = new Color(0xFF, 0xF1, 0xD6);
    private static final Color BASE_LINE = new Color(0xB2, 0x6A, 0x00);

    public static void main(String[] args) throws IOException {
        File out = new File(args.length > 0 ? args[0] : ".");
        out.mkdirs();
        recursionTree(new File(out, "arvore-recursao-pequena.png"), 10, KnapsackInstances.createSmallInstance());
        flowchart(new File(out, "fluxograma-dc-mochila.png"));
        System.out.println("Figuras geradas em " + out.getAbsolutePath());
    }

    // ------------------------------------------------------------------ árvore de recursão

    private static final class Node {
        int id, depth, itemIndex;
        double remaining, value, bound, bestBefore;
        String edgeLabel;
        boolean pruned, divided, newBest;
        final List<Node> children = new ArrayList<>();
        double x;
    }

    private static int counter;
    private static double best;

    private static Node search(List<KnapsackItem> items, int idx, double rem, double cur, int depth, String edge) {
        Node n = new Node();
        n.id = ++counter;
        n.depth = depth;
        n.itemIndex = idx;
        n.remaining = rem;
        n.value = cur;
        n.edgeLabel = edge;
        if (cur > best) { best = cur; n.newBest = true; }
        n.bestBefore = best;
        if (idx >= items.size()) return n;
        n.bound = bound(items, idx, rem, cur);
        if (n.bound <= best) { n.pruned = true; return n; }
        n.divided = true;
        KnapsackItem it = items.get(idx);
        if (it.getWeight() <= rem) {
            n.children.add(search(items, idx + 1, rem - it.getWeight(), cur + it.getValue(), depth + 1,
                    "levar " + it.getName()));
        }
        n.children.add(search(items, idx + 1, rem, cur, depth + 1, "não levar " + it.getName()));
        return n;
    }

    private static double bound(List<KnapsackItem> items, int idx, double rem, double cur) {
        double ub = cur, cap = rem;
        for (int i = idx; i < items.size() && cap > 0; i++) {
            KnapsackItem it = items.get(i);
            if (it.getValue() <= 0) continue;
            if (it.getWeight() <= cap) { ub += it.getValue(); cap -= it.getWeight(); }
            else { ub += it.getValue() * (cap / it.getWeight()); break; }
        }
        return ub;
    }

    private static double leafCursor;

    private static void layout(Node n) {
        if (n.children.isEmpty()) { n.x = leafCursor++; return; }
        for (Node c : n.children) layout(c);
        n.x = (n.children.get(0).x + n.children.get(n.children.size() - 1).x) / 2.0;
    }

    private static int maxDepth(Node n) {
        int d = n.depth;
        for (Node c : n.children) d = Math.max(d, maxDepth(c));
        return d;
    }

    private static void recursionTree(File file, double capacity, List<KnapsackItem> items) throws IOException {
        List<KnapsackItem> ordered = new ArrayList<>(items);
        ordered.sort(Comparator.comparingDouble((KnapsackItem i) -> i.getValue() / i.getWeight()).reversed());
        counter = 0; best = 0; leafCursor = 0;
        Node root = search(ordered, 0, capacity, 0, 0, null);
        layout(root);

        int boxW = 210, boxH = 92, colW = 250, rowH = 165, marginX = 40, top = 120;
        int width = (int) (marginX * 2 + leafCursor * colW);
        int height = top + (maxDepth(root) + 1) * rowH + 110;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas(img);

        g.setFont(TITLE);
        g.setColor(INK);
        StringBuilder order = new StringBuilder();
        for (KnapsackItem it : ordered) {
            if (order.length() > 0) order.append(", ");
            order.append(String.format("%s(%.0f,%.0f)", it.getName(), it.getWeight(), it.getValue()));
        }
        centered(g, "Árvore de recursão — instância pequena (W = " + (int) capacity + ")", width / 2, 40);
        g.setFont(PLAIN);
        centered(g, "Itens na ordem de razão valor/peso: " + order + "   ·   (peso, valor)", width / 2, 70);

        drawTree(g, root, ordered, marginX, top, colW, rowH, boxW, boxH);

        int ly = height - 60;
        int lx = marginX;
        lx = legend(g, lx, ly, DIVIDE, DIVIDE_LINE, "divide em levar / não levar");
        lx = legend(g, lx, ly, BEST, BEST_LINE, "melhor solução encontrada (valor 49)");
        legend(g, lx, ly, PRUNE, PRUNE_LINE, "caso base: limite ≤ melhor → podado");
        g.dispose();
        ImageIO.write(img, "png", file);
    }

    private static void drawTree(Graphics2D g, Node n, List<KnapsackItem> items, int marginX, int top,
                                 int colW, int rowH, int boxW, int boxH) {
        int cx = (int) (marginX + n.x * colW + colW / 2.0);
        int y = top + n.depth * rowH;
        for (Node c : n.children) {
            int ccx = (int) (marginX + c.x * colW + colW / 2.0);
            int cy = top + c.depth * rowH;
            g.setColor(INK);
            g.setStroke(new BasicStroke(1.6f));
            g.drawLine(cx, y + boxH, ccx, cy);
            g.setFont(BOLD);
            String label = c.edgeLabel;
            int mx = (cx + ccx) / 2, my = (y + boxH + cy) / 2;
            int tw = g.getFontMetrics().stringWidth(label);
            g.setColor(Color.WHITE);
            g.fillRect(mx - tw / 2 - 4, my - 13, tw + 8, 20);
            g.setColor(c.edgeLabel.startsWith("levar") ? BEST_LINE : new Color(0xA0, 0x30, 0x30));
            centered(g, label, mx, my + 2);
            drawTree(g, c, items, marginX, top, colW, rowH, boxW, boxH);
        }

        Color fill, line;
        if (n.newBest && !n.divided) { fill = BEST; line = BEST_LINE; }
        else if (n.divided) { fill = DIVIDE; line = DIVIDE_LINE; }
        else { fill = PRUNE; line = PRUNE_LINE; }
        int x = cx - boxW / 2;
        g.setColor(fill);
        g.fillRoundRect(x, y, boxW, boxH, 16, 16);
        g.setColor(line);
        g.setStroke(new BasicStroke(2.2f));
        g.drawRoundRect(x, y, boxW, boxH, 16, 16);

        String next = n.itemIndex < items.size() ? items.get(n.itemIndex).getName() : "—";
        g.setColor(INK);
        g.setFont(BOLD);
        centered(g, "#" + n.id + "  item " + next + " · c = " + fmt(n.remaining), cx, y + 24);
        g.setFont(PLAIN);
        centered(g, "valor = " + fmt(n.value) + " · limite = " + fmt(n.bound), cx, y + 46);
        String status = n.divided ? "limite > melhor (" + fmt(n.bestBefore) + ") → divide"
                : "limite ≤ melhor (" + fmt(n.bestBefore) + ") → poda";
        centered(g, status, cx, y + 68);
        if (n.newBest && !n.divided) {
            g.setFont(BOLD);
            g.setColor(BEST_LINE);
            centered(g, "solução final {A, C, B} = " + fmt(n.value), cx, y + boxH + 22);
        }
    }

    // ------------------------------------------------------------------ fluxograma

    private static void flowchart(File file) throws IOException {
        int width = 1100, height = 1180;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas(img);
        g.setColor(INK);
        g.setFont(TITLE);
        centered(g, "Divisão e Conquista aplicada à mochila 0/1", width / 2, 42);

        int cx = 520, bw = 520;
        box(g, cx, 80, bw, 80, DIVIDE, DIVIDE_LINE, "PROBLEMA ORIGINAL",
                "itens ordenados por valor/peso; resolver (i = 1, c = W)");
        arrow(g, cx, 160, cx, 200);
        box(g, cx, 200, bw, 80, DIVIDE, DIVIDE_LINE, "SUBPROBLEMA (i, c, valor acumulado)",
                "atualiza a melhor solução se valor > melhor");
        arrow(g, cx, 280, cx, 320);
        diamond(g, cx, 320, 460, 170, "CASO BASE?",
                "acabaram os itens  ou", "limite fracionário ≤ melhor?");
        // SIM → direita
        g.setFont(BOLD);
        g.setColor(BEST_LINE);
        g.drawString("SIM", cx + 240, 395);
        arrow(g, cx + 230, 405, 870, 405);
        box(g, 965, 360, 190, 90, BASE, BASE_LINE, "RETORNA", "sem novas divisões");
        // NÃO → baixo
        g.setColor(new Color(0xA0, 0x30, 0x30));
        g.setFont(BOLD);
        g.drawString("NÃO", cx + 12, 515);
        arrow(g, cx, 490, cx, 530);
        box(g, cx, 530, bw, 80, DIVIDE, DIVIDE_LINE, "DIVIDIR pelo item i",
                "duas mochilas menores, com um item a menos");
        arrow(g, cx - 130, 610, 330, 660);
        arrow(g, cx + 130, 610, 710, 660);
        box(g, 330, 660, 330, 80, BEST, BEST_LINE, "LEVAR i (se wᵢ ≤ c)", "(i+1, c − wᵢ, valor + vᵢ)");
        box(g, 710, 660, 330, 80, PRUNE, PRUNE_LINE, "NÃO LEVAR i", "(i+1, c, valor)");
        arrow(g, 330, 740, cx - 100, 790);
        arrow(g, 710, 740, cx + 100, 790);
        box(g, cx, 790, bw, 80, BASE, BASE_LINE, "CONQUISTAR",
                "resolve cada ramo pela mesma recursão");
        // laço de volta ao subproblema
        g.setColor(INK);
        g.setStroke(new BasicStroke(2f));
        g.drawLine(cx - bw / 2, 830, 70, 830);
        g.drawLine(70, 830, 70, 240);
        arrow(g, 70, 240, cx - bw / 2, 240);
        g.setFont(PLAIN);
        g.drawString("recursão", 80, 540);
        // retorno do caso base para combinar
        g.drawLine(965, 450, 965, 950);
        arrow(g, 965, 950, cx + bw / 2, 950);
        g.drawString("retorno", 975, 700);
        arrow(g, cx, 870, cx, 910);
        box(g, cx, 910, bw, 80, DIVIDE, DIVIDE_LINE, "COMBINAR",
                "fica a melhor entre levar e não levar: max(…)");
        arrow(g, cx, 990, cx, 1030);
        box(g, cx, 1030, bw, 80, BEST, BEST_LINE, "SOLUÇÃO DO PROBLEMA ORIGINAL",
                "maior valor e itens escolhidos para (1, W)");
        g.dispose();
        ImageIO.write(img, "png", file);
    }

    // ------------------------------------------------------------------ utilitários de desenho

    private static Graphics2D canvas(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        return g;
    }

    private static void box(Graphics2D g, int cx, int y, int w, int h, Color fill, Color line,
                            String title, String text) {
        int x = cx - w / 2;
        g.setColor(fill);
        g.fillRoundRect(x, y, w, h, 18, 18);
        g.setColor(line);
        g.setStroke(new BasicStroke(2.4f));
        g.drawRoundRect(x, y, w, h, 18, 18);
        g.setColor(INK);
        g.setFont(BOLD);
        centered(g, title, cx, y + h / 2 - 6);
        g.setFont(PLAIN);
        centered(g, text, cx, y + h / 2 + 18);
    }

    private static void diamond(Graphics2D g, int cx, int y, int w, int h, String title, String l1, String l2) {
        Path2D p = new Path2D.Double();
        p.moveTo(cx, y);
        p.lineTo(cx + w / 2.0, y + h / 2.0);
        p.lineTo(cx, y + h);
        p.lineTo(cx - w / 2.0, y + h / 2.0);
        p.closePath();
        g.setColor(new Color(0xEE, 0xE6, 0xF6));
        g.fill(p);
        g.setColor(new Color(0x5E, 0x35, 0x8C));
        g.setStroke(new BasicStroke(2.4f));
        g.draw(p);
        g.setColor(INK);
        g.setFont(BOLD);
        centered(g, title, cx, y + h / 2 - 18);
        g.setFont(PLAIN);
        centered(g, l1, cx, y + h / 2 + 6);
        centered(g, l2, cx, y + h / 2 + 26);
    }

    private static void arrow(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.setColor(INK);
        g.setStroke(new BasicStroke(2f));
        g.drawLine(x1, y1, x2, y2);
        double a = Math.atan2(y2 - y1, x2 - x1);
        int s = 11;
        Polygon head = new Polygon();
        head.addPoint(x2, y2);
        head.addPoint((int) (x2 - s * Math.cos(a - 0.4)), (int) (y2 - s * Math.sin(a - 0.4)));
        head.addPoint((int) (x2 - s * Math.cos(a + 0.4)), (int) (y2 - s * Math.sin(a + 0.4)));
        g.fillPolygon(head);
    }

    private static int legend(Graphics2D g, int x, int y, Color fill, Color line, String text) {
        g.setColor(fill);
        g.fillRoundRect(x, y, 28, 20, 6, 6);
        g.setColor(line);
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(x, y, 28, 20, 6, 6);
        g.setColor(INK);
        g.setFont(PLAIN);
        g.drawString(text, x + 38, y + 15);
        return x + 38 + g.getFontMetrics().stringWidth(text) + 40;
    }

    private static void centered(Graphics2D g, String s, int cx, int baseline) {
        g.drawString(s, cx - g.getFontMetrics().stringWidth(s) / 2, baseline);
    }

    private static String fmt(double v) {
        return v == Math.rint(v) ? String.valueOf((long) v) : String.format("%.1f", v).replace('.', ',');
    }
}
