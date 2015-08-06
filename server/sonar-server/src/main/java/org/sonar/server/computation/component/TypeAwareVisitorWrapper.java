/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.computation.component;

public class TypeAwareVisitorWrapper implements VisitorWrapper {

  private final TypeAwareVisitor delegate;

  public TypeAwareVisitorWrapper(TypeAwareVisitor delegate) {
    this.delegate = delegate;
  }

  @Override
  public void beforeComponent(Component component){
    // Nothing to do
  }

  @Override
  public void afterComponent(Component component){
    // Nothing to do
  }

  @Override
  public void visitProject(Component tree) {
    delegate.visitProject(tree);
  }

  @Override
  public void visitModule(Component tree) {
    delegate.visitModule(tree);
  }

  @Override
  public void visitDirectory(Component tree) {
    delegate.visitDirectory(tree);
  }

  @Override
  public void visitFile(Component tree) {
    delegate.visitFile(tree);
  }

  @Override
  public void visitAny(Component component) {
    delegate.visitAny(component);
  }

  @Override
  public Visitor.Order getOrder() {
    return delegate.getOrder();
  }

  @Override
  public Component.Type getMaxDepth() {
    return delegate.getMaxDepth();
  }

}
