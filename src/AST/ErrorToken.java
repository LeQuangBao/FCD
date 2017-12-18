package AST;

public class ErrorToken extends RuntimeException {
  public String s;

  public ErrorToken(String s) {
    this.s = s;
  }

  @Override
  public String toString() {
    return "Error token: " + this.s;
  }
}
