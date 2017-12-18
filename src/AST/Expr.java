package AST;

public abstract class Expr {
  public Object accept(BaseVisitor v, Object o) {
    return v.visit(this, o);
  }
}
