package org.carrot2.util.attrs;

public abstract class AttrComposite implements AcceptingVisitor {
  protected final AttrGroup attributes = new AttrGroup();

  @Override
  public void accept(AttrVisitor visitor) {
    attributes.visit(visitor);
  }
}
