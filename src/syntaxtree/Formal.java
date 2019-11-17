package syntaxtree;
import visitor.Visitor;

/**
 * Objects of this class represent a formal parameter:
 * <b>type id</b>
 */
public class Formal {
    
  public Type type;
  public Identifier id;
 
  public Formal(Type type, Identifier id) {
    this.type=type; 
    this.id=id;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }
}
