package planet.util;

/**
 * @author Ruben Mondejar
 *
 */

public class KeyGen implements java.io.Serializable {

  private int cnt, ini, MAX;

  public KeyGen (int ini_value, int MAX) {
    cnt = ini = ini_value;
    this.MAX = MAX;
  }

  public String generateKey() {
    cnt++;
    if (cnt>MAX) cnt = ini;

    return Integer.toString(cnt);
  }
}
