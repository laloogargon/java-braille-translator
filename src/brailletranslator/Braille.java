package brailletranslator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Braille {

    private String cadena;
    private String cad;
    private char[] arreglo;

    public Braille(String ca) {
        this.cadena = ca;
        arreglo = cadena.toCharArray();
    }

    public String checarCadena() {
        cad = "";
        ArrayList<String> var = new ArrayList();
        StringTokenizer tokens = new StringTokenizer(cadena);
        while (tokens.hasMoreTokens()) {
            var.add(tokens.nextToken());
        }
        for (String aux : var) {
            int con = 0;
            for (int i = 0; i < aux.length(); i++) {
                if (Character.isUpperCase(aux.charAt(i))) {
                    con++;
                }
                if (Character.isDigit(aux.charAt(i))) {
                    con++;
                }
            }
            int v = aux.length() + con;
            con = 0;
            if (v > 11) {
                cad = "-------> Algunos elementos no caben, fueron divididos <-------";
            }
        }
        return cad;
    }

    public void mandarPdf() {
        Document docu = new Document();
        try {
            PdfWriter.getInstance(docu, new FileOutputStream("translation.pdf"));
            docu.open();
            Paragraph parrafo = new Paragraph("CÓDIGO BRAILE");
            parrafo.setAlignment(Element.ALIGN_CENTER);
            docu.add(parrafo);
            int x = 25;
            int y = 600;
            int confila = 0;
            int val = 0;
            File ar = new File(".");
            String dir = ar.getCanonicalPath() + "\\images\\";
            Paragraph parrafo2 = new Paragraph("\nSe muestra el código de la siguiente cadena:");
            parrafo2.setAlignment(Element.ALIGN_CENTER);
            docu.add(parrafo2);
            Paragraph parrafo3 = new Paragraph(cadena);
            parrafo3.setAlignment(Element.ALIGN_CENTER);
            docu.add(parrafo3);
            Paragraph parrafo4 = new Paragraph(checarCadena());
            parrafo4.setAlignment(Element.ALIGN_CENTER);
            docu.add(parrafo4);
            String valores = "1234567890abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZáéíóú¡!&,.()-;ü";
            for (int i = 0; i < arreglo.length; i++) {
                if (Character.isUpperCase(arreglo[i]) == true) {
                    Image ima = Image.getInstance(dir + "mayus.png");
                    ima.setAbsolutePosition(x, y);
                    docu.add(ima);
                    x = x + 50;
                    confila++;
                }
                if (Character.isDigit(arreglo[i]) == true) {
                    Image imo = Image.getInstance(dir + "number.png");
                    imo.setAbsolutePosition(x, y);
                    docu.add(imo);
                    x = x + 50;
                    confila++;
                }
                for (int j = 0; j < valores.length(); j++) {
                    if (arreglo[i] == valores.charAt(j)) {
                        Image imm = Image.getInstance(dir + arreglo[i] + ".png");
                        imm.setAbsolutePosition(x, y);
                        docu.add(imm);
                        x = x + 50;
                        confila++;
                    }
                }
                if (arreglo[i] == '*') {
                    Image imm = Image.getInstance(dir + "asterisco.png");
                    imm.setAbsolutePosition(x, y);
                    docu.add(imm);
                    x = x + 50;
                    confila++;
                }
                if (arreglo[i] == '"') {
                    Image imm = Image.getInstance(dir + "comillas.png");
                    imm.setAbsolutePosition(x, y);
                    docu.add(imm);
                    x = x + 50;
                    confila++;
                }
                if (arreglo[i] == '¿' || arreglo[i] == '?') {
                    Image imm = Image.getInstance(dir + "interrogacion.png");
                    imm.setAbsolutePosition(x, y);
                    docu.add(imm);
                    x = x + 50;
                    confila++;
                }
                if (val == -1) {
                    if (arreglo[i] == ' ') {
                        val = 0;
                        confila = 0;
                    }
                }
                else if (val != -1) {
                    if (arreglo[i] == ' ') {
                        y = y - 90;
                        x = 25;
                        confila = 0;
                    }
                }
                if (y <= 25) {
                    docu.newPage();
                    x = 25;
                    y = 740;
                    confila = 0;
                }
                if (confila == 11) {
                    y = y - 90;
                    x = 25;
                    confila = 0;
                    val = -1;
                }
            }

            docu.close();
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(BrailleTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
