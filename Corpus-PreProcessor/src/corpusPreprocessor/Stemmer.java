package corpusPreprocessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class Stemmer {
  private char[] b = new char[50];
  
  private int i = 0;
  
  private int i_end = 0;
  
  private int j;
  
  private int k;
  
  private static final int INC = 50;
  
  public void add(char ch) {
    if (this.i == this.b.length) {
      char[] new_b = new char[this.i + 50];
      for (int c = 0; c < this.i; c++)
        new_b[c] = this.b[c]; 
      this.b = new_b;
    } 
    this.b[this.i++] = ch;
  }
  
  public void add(char[] w, int wLen) {
    if (this.i + wLen >= this.b.length) {
      char[] new_b = new char[this.i + wLen + 50];
      for (int i = 0; i < this.i; i++)
        new_b[i] = this.b[i]; 
      this.b = new_b;
    } 
    for (int c = 0; c < wLen; c++)
      this.b[this.i++] = w[c]; 
  }
  
  public String toString() {
    return new String(this.b, 0, this.i_end);
  }
  
  public int getResultLength() {
    return this.i_end;
  }
  
  public char[] getResultBuffer() {
    return this.b;
  }
  
  private final boolean cons(int i) {
    switch (this.b[i]) {
      case 'a':
      case 'e':
      case 'i':
      case 'o':
      case 'u':
        return false;
      case 'y':
        return (i == 0) ? true : (!cons(i - 1));
    } 
    return true;
  }
  
  private final int m() {
    int n = 0;
    int i = 0;
    while (true) {
      if (i > this.j)
        return n; 
      if (!cons(i))
        break; 
      i++;
    } 
    i++;
    while (true) {
      if (i > this.j)
        return n; 
      if (cons(i)) {
        i++;
        n++;
        while (true) {
          if (i > this.j)
            return n; 
          if (!cons(i))
            break; 
          i++;
        } 
        i++;
        continue;
      } 
      i++;
    } 
  }
  
  private final boolean vowelinstem() {
    for (int i = 0; i <= this.j; i++) {
      if (!cons(i))
        return true; 
    } 
    return false;
  }
  
  private final boolean doublec(int j) {
    if (j < 1)
      return false; 
    if (this.b[j] != this.b[j - 1])
      return false; 
    return cons(j);
  }
  
  private final boolean cvc(int i) {
    if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
      return false; 
    int ch = this.b[i];
    if (ch == 119 || ch == 120 || ch == 121)
      return false; 
    return true;
  }
  
  private final boolean ends(String s) {
    int l = s.length();
    int o = this.k - l + 1;
    if (o < 0)
      return false; 
    for (int i = 0; i < l; i++) {
      if (this.b[o + i] != s.charAt(i))
        return false; 
    } 
    this.j = this.k - l;
    return true;
  }
  
  private final void setto(String s) {
    int l = s.length();
    int o = this.j + 1;
    for (int i = 0; i < l; i++)
      this.b[o + i] = s.charAt(i); 
    this.k = this.j + l;
  }
  
  private final void r(String s) {
    if (m() > 0)
      setto(s); 
  }
  
  private final void step1() {
    if (this.b[this.k] == 's')
      if (ends("sses")) {
        this.k -= 2;
      } else if (ends("ies")) {
        setto("i");
      } else if (this.b[this.k - 1] != 's') {
        this.k--;
      }  
    if (ends("eed")) {
      if (m() > 0)
        this.k--; 
    } else if ((ends("ed") || ends("ing")) && vowelinstem()) {
      this.k = this.j;
      if (ends("at")) {
        setto("ate");
      } else if (ends("bl")) {
        setto("ble");
      } else if (ends("iz")) {
        setto("ize");
      } else if (doublec(this.k)) {
        this.k--;
        int ch = this.b[this.k];
        if (ch == 108 || ch == 115 || ch == 122)
          this.k++; 
      } else if (m() == 1 && cvc(this.k)) {
        setto("e");
      } 
    } 
  }
  
  private final void step2() {
    if (ends("y") && vowelinstem())
      this.b[this.k] = 'i'; 
  }
  
  private final void step3() {
    if (this.k == 0)
      return; 
    switch (this.b[this.k - 1]) {
      case 'a':
        if (ends("ational")) {
          r("ate");
          break;
        } 
        if (ends("tional"))
          r("tion"); 
        break;
      case 'c':
        if (ends("enci")) {
          r("ence");
          break;
        } 
        if (ends("anci"))
          r("ance"); 
        break;
      case 'e':
        if (ends("izer"))
          r("ize"); 
        break;
      case 'l':
        if (ends("bli")) {
          r("ble");
          break;
        } 
        if (ends("alli")) {
          r("al");
          break;
        } 
        if (ends("entli")) {
          r("ent");
          break;
        } 
        if (ends("eli")) {
          r("e");
          break;
        } 
        if (ends("ousli"))
          r("ous"); 
        break;
      case 'o':
        if (ends("ization")) {
          r("ize");
          break;
        } 
        if (ends("ation")) {
          r("ate");
          break;
        } 
        if (ends("ator"))
          r("ate"); 
        break;
      case 's':
        if (ends("alism")) {
          r("al");
          break;
        } 
        if (ends("iveness")) {
          r("ive");
          break;
        } 
        if (ends("fulness")) {
          r("ful");
          break;
        } 
        if (ends("ousness"))
          r("ous"); 
        break;
      case 't':
        if (ends("aliti")) {
          r("al");
          break;
        } 
        if (ends("iviti")) {
          r("ive");
          break;
        } 
        if (ends("biliti"))
          r("ble"); 
        break;
      case 'g':
        if (ends("logi"))
          r("log"); 
        break;
    } 
  }
  
  private final void step4() {
    switch (this.b[this.k]) {
      case 'e':
        if (ends("icate")) {
          r("ic");
          break;
        } 
        if (ends("ative")) {
          r("");
          break;
        } 
        if (ends("alize"))
          r("al"); 
        break;
      case 'i':
        if (ends("iciti"))
          r("ic"); 
        break;
      case 'l':
        if (ends("ical")) {
          r("ic");
          break;
        } 
        if (ends("ful"))
          r(""); 
        break;
      case 's':
        if (ends("ness"))
          r(""); 
        break;
    } 
  }
  
  private final void step5() {
    if (this.k == 0)
      return; 
    switch (this.b[this.k - 1]) {
      case 'a':
        if (ends("al"))
          break; 
        return;
      case 'c':
        if (ends("ance"))
          break; 
        if (ends("ence"))
          break; 
        return;
      case 'e':
        if (ends("er"))
          break; 
        return;
      case 'i':
        if (ends("ic"))
          break; 
        return;
      case 'l':
        if (ends("able"))
          break; 
        if (ends("ible"))
          break; 
        return;
      case 'n':
        if (ends("ant"))
          break; 
        if (ends("ement"))
          break; 
        if (ends("ment"))
          break; 
        if (ends("ent"))
          break; 
        return;
      case 'o':
        if (ends("ion") && this.j >= 0 && (this.b[this.j] == 's' || this.b[this.j] == 't'))
          break; 
        if (ends("ou"))
          break; 
        return;
      case 's':
        if (ends("ism"))
          break; 
        return;
      case 't':
        if (ends("ate"))
          break; 
        if (ends("iti"))
          break; 
        return;
      case 'u':
        if (ends("ous"))
          break; 
        return;
      case 'v':
        if (ends("ive"))
          break; 
        return;
      case 'z':
        if (ends("ize"))
          break; 
        return;
      default:
        return;
    } 
    if (m() > 1)
      this.k = this.j; 
  }
  
  private final void step6() {
    this.j = this.k;
    if (this.b[this.k] == 'e') {
      int a = m();
      if (a > 1 || (a == 1 && !cvc(this.k - 1)))
        this.k--; 
    } 
    if (this.b[this.k] == 'l' && doublec(this.k) && m() > 1)
      this.k--; 
  }
  
  public void stem() {
    this.k = this.i - 1;
    if (this.k > 1) {
      step1();
      step2();
      step3();
      step4();
      step5();
      step6();
    } 
    this.i_end = this.k + 1;
    this.i = 0;
  }
  
  public static void main(String[] args) {
    char[] w = new char[501];
    corpusPreprocessor.Stemmer s = new corpusPreprocessor.Stemmer();
    args = new String[] { "d:\\FLT\\Data\\Input\\Corpora\\Queries-Rhino-NounsOnly-NoStem.txt" };
    for (int i = 0; i < args.length;) {
      try {
        FileInputStream in = new FileInputStream(args[i]);
        try {
          while (true) {
            int ch = in.read();
            if (Character.isLetter((char)ch)) {
              int j = 0;
              do {
                ch = Character.toLowerCase((char)ch);
                w[j] = (char)ch;
                if (j < 500)
                  j++; 
                ch = in.read();
              } while (Character.isLetter((char)ch));
              for (int c = 0; c < j; c++)
                s.add(w[c]); 
              s.stem();
              String u = s.toString();
              System.out.print(u);
            } 
            if (ch < 0) {
              i++;
              continue;
            } 
            System.out.print((char)ch);
          } 
        } catch (IOException e) {
          System.out.println("error reading " + args[i]);
          break;
        } 
      } catch (FileNotFoundException e) {
        System.out.println("file " + args[i] + " not found");
        break;
      } 
    } 
  }
}

