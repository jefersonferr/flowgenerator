package org.flowgenerator.domain;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JTextArea;

import org.flowgenerator.gui.FlowGenerator;

/**
 * Grammar to parse Java version 1.5
 * @author Sreenivasa Viswanadha - Simplified and enhanced for 1.5
 */
public class JavaParser implements JavaParserConstants {
        
   /* FlowGenerator - inicio */        
   static GraphGenerator gfc;
   /* FlowGenerator - fim */        
   /**
    * Class to hold modifiers.
    */
   static public final class ModifierSet
   {
     /* Definitions of the bits in the modifiers field.  */
     public static final int PUBLIC = 0x0001;
     public static final int PROTECTED = 0x0002;
     public static final int PRIVATE = 0x0004;
     public static final int ABSTRACT = 0x0008;
     public static final int STATIC = 0x0010;
     public static final int FINAL = 0x0020;
     public static final int SYNCHRONIZED = 0x0040;
     public static final int NATIVE = 0x0080;
     public static final int TRANSIENT = 0x0100;
     public static final int VOLATILE = 0x0200;
     public static final int STRICTFP = 0x1000;

     /** A set of accessors that indicate whether the specified modifier
         is in the set. */

     public boolean isPublic(int modifiers)
     {
       return (modifiers & PUBLIC) != 0;
     }

     public boolean isProtected(int modifiers)
     {
       return (modifiers & PROTECTED) != 0;
     }

     public boolean isPrivate(int modifiers)
     {
       return (modifiers & PRIVATE) != 0;
     }

     public boolean isStatic(int modifiers)
     {
       return (modifiers & STATIC) != 0;
     }

     public boolean isAbstract(int modifiers)
     {
       return (modifiers & ABSTRACT) != 0;
     }

     public boolean isFinal(int modifiers)
     {
       return (modifiers & FINAL) != 0;
     }

     public boolean isNative(int modifiers)
     {
       return (modifiers & NATIVE) != 0;
     }

     public boolean isStrictfp(int modifiers)
     {
       return (modifiers & STRICTFP) != 0;
     }

     public boolean isSynchronized(int modifiers)
     {
       return (modifiers & SYNCHRONIZED) != 0;
     }

     public boolean isTransient(int modifiers)
      {
       return (modifiers & TRANSIENT) != 0;
     }

     public boolean isVolatile(int modifiers)
     {
       return (modifiers & VOLATILE) != 0;
     }

     /**
      * Removes the given modifier.
      */
     static int removeModifier(int modifiers, int mod)
     {
        return modifiers & ~mod;
     }
   }

   public JavaParser(String fileName)
   {
      this(System.in);
      try { ReInit(new FileInputStream(new File(fileName))); }
      catch(Exception e) { e.printStackTrace(); }
   }

   public static void main(String args, JTextArea c) {
    JavaParser parser;
    c.append(FlowGenerator.mlp.getString("reading_file")+" "+ args + " . . .  ");
      try {
    	     parser = new JavaParser(new java.io.FileInputStream(args));
      } catch (java.io.FileNotFoundException e) {
    	  c.append(FlowGenerator.mlp.getString("java_parser_file")+" " + args + FlowGenerator.mlp.getString("not_found"));
        return;
      }
    try {
      /* FlowGenerator - inicio */
      gfc = new GraphGenerator(args.replace(".java",""));
      /* FlowGenerator - fim */
      parser.CompilationUnit();
      /* FlowGenerator - inicio */
      gfc.geraDot();
      gfc.geraHtml();
      /* FlowGenerator - fim */
      c.append(FlowGenerator.mlp.getString("parsing_sucess"));
    } catch (ParseException e) {
      c.append(e.getMessage());
      c.append(FlowGenerator.mlp.getString("parsing_errors"));
    }
  }

/*****************************************
 * THE JAVA LANGUAGE GRAMMAR STARTS HERE *
 *****************************************/

/*
 * Program structuring syntax follows.
 */
  final public void CompilationUnit() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PACKAGE:
      PackageDeclaration();
      break;
    default:
      ;
    }
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IMPORT:
        ;
        break;
      default:
        break label_1;
      }
      ImportDeclaration();
    }
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ABSTRACT:
      case CLASS:
      case ENUM:
      case FINAL:
      case INTERFACE:
      case NATIVE:
      case PRIVATE:
      case PROTECTED:
      case PUBLIC:
      case STATIC:
      case STRICTFP:
      case SYNCHRONIZED:
      case TRANSIENT:
      case VOLATILE:
      case SEMICOLON:
      case AT:
        ;
        break;
      default:
        break label_2;
      }
      TypeDeclaration();
    }
    jj_consume_token(0);
  }

  final public void PackageDeclaration() throws ParseException {
    jj_consume_token(PACKAGE);
    Name();
    jj_consume_token(SEMICOLON);
  }

  final public void ImportDeclaration() throws ParseException {
    jj_consume_token(IMPORT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STATIC:
      jj_consume_token(STATIC);
      break;
    default:
      ;
    }
    Name();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DOT:
      jj_consume_token(DOT);
      jj_consume_token(STAR);
      break;
    default:
      ;
    }
    jj_consume_token(SEMICOLON);
  }

/*
 * Modifiers. We match all modifiers in a single rule to reduce the chances of
 * syntax errors for simple modifier mistakes. It will also enable us to give
 * better error messages.
 */
  @SuppressWarnings("unused")
final public int Modifiers() throws ParseException {
   int modifiers = 0;
    label_3:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
        jj_consume_token(PUBLIC);
              modifiers |= ModifierSet.PUBLIC;
        break;
      case STATIC:
        jj_consume_token(STATIC);
              modifiers |= ModifierSet.STATIC;
        break;
      case PROTECTED:
        jj_consume_token(PROTECTED);
                 modifiers |= ModifierSet.PROTECTED;
        break;
      case PRIVATE:
        jj_consume_token(PRIVATE);
               modifiers |= ModifierSet.PRIVATE;
        break;
      case FINAL:
        jj_consume_token(FINAL);
             modifiers |= ModifierSet.FINAL;
        break;
      case ABSTRACT:
        jj_consume_token(ABSTRACT);
                modifiers |= ModifierSet.ABSTRACT;
        break;
      case SYNCHRONIZED:
        jj_consume_token(SYNCHRONIZED);
                    modifiers |= ModifierSet.SYNCHRONIZED;
        break;
      case NATIVE:
        jj_consume_token(NATIVE);
              modifiers |= ModifierSet.NATIVE;
        break;
      case TRANSIENT:
        jj_consume_token(TRANSIENT);
                 modifiers |= ModifierSet.TRANSIENT;
        break;
      case VOLATILE:
        jj_consume_token(VOLATILE);
                modifiers |= ModifierSet.VOLATILE;
        break;
      case STRICTFP:
        jj_consume_token(STRICTFP);
                modifiers |= ModifierSet.STRICTFP;
        break;
      case AT:
        Annotation();
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return modifiers;}
    throw new Error("Missing return statement in function");
  }

/*
 * Declaration syntax follows.
 */
  final public void TypeDeclaration() throws ParseException {
   int modifiers;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    case ABSTRACT:
    case CLASS:
    case ENUM:
    case FINAL:
    case INTERFACE:
    case NATIVE:
    case PRIVATE:
    case PROTECTED:
    case PUBLIC:
    case STATIC:
    case STRICTFP:
    case SYNCHRONIZED:
    case TRANSIENT:
    case VOLATILE:
    case AT:
      modifiers = Modifiers();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
      case INTERFACE:
        ClassOrInterfaceDeclaration(modifiers);
        break;
      case ENUM:
        EnumDeclaration(modifiers);
        break;
      case AT:
        AnnotationTypeDeclaration(modifiers);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void ClassOrInterfaceDeclaration(int modifiers) throws ParseException {
   boolean isInterface = false;
   String  label = new String();    /* FlowGenerator */
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CLASS:
      jj_consume_token(CLASS);
      break;
    case INTERFACE:
      jj_consume_token(INTERFACE);
                            isInterface = true;
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    /* FlowGenerator - inicio */
    label = label+getToken(jj_ntk)+" ";
    jj_consume_token(IDENTIFIER);
    label = label+getToken(jj_ntk);
    gfc.openClusterClass(label);
    /* FlowGenerator - fim */

    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LT:
      TypeParameters();
      break;
    default:
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXTENDS:
      ExtendsList(isInterface);
      break;
    default:
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IMPLEMENTS:
      ImplementsList(isInterface);
      break;
    default:
      ;
    }
    ClassOrInterfaceBody(isInterface);
    gfc.closeClusterClass(); /* FlowGenerator */
  }

  final public void ExtendsList(boolean isInterface) throws ParseException {
   boolean extendsMoreThanOne = false;
    jj_consume_token(EXTENDS);
    ClassOrInterfaceType();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_4;
      }
      jj_consume_token(COMMA);
      ClassOrInterfaceType();
                                  extendsMoreThanOne = true;
    }
      if (extendsMoreThanOne && !isInterface)
         {if (true) throw new ParseException("A class cannot extend more than one other class");}
  }

  final public void ImplementsList(boolean isInterface) throws ParseException {
    jj_consume_token(IMPLEMENTS);
    ClassOrInterfaceType();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_5;
      }
      jj_consume_token(COMMA);
      ClassOrInterfaceType();
    }
      if (isInterface)
         {if (true) throw new ParseException("An interface cannot implement other interfaces");}
  }

  final public void EnumDeclaration(int modifiers) throws ParseException {
    jj_consume_token(ENUM);
    jj_consume_token(IDENTIFIER);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IMPLEMENTS:
      ImplementsList(false);
      break;
    default:
      ;
    }
    EnumBody();
  }

  final public void EnumBody() throws ParseException {
    jj_consume_token(LBRACE);
    EnumConstant();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_6;
      }
      jj_consume_token(COMMA);
      EnumConstant();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ABSTRACT:
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case CLASS:
        case DOUBLE:
        case ENUM:
        case FINAL:
        case FLOAT:
        case INT:
        case INTERFACE:
        case LONG:
        case NATIVE:
        case PRIVATE:
        case PROTECTED:
        case PUBLIC:
        case SHORT:
        case STATIC:
        case STRICTFP:
        case SYNCHRONIZED:
        case TRANSIENT:
        case VOID:
        case VOLATILE:
        case IDENTIFIER:
        case LBRACE:
        case SEMICOLON:
        case AT:
        case LT:
          ;
          break;
        default:
          break label_7;
        }
        ClassOrInterfaceBodyDeclaration(false);
      }
      break;
    default:
      ;
    }
    jj_consume_token(RBRACE);
  }

  final public void EnumConstant() throws ParseException {
    jj_consume_token(IDENTIFIER);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      Arguments();
      break;
    default:
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
      ClassOrInterfaceBody(false);
      break;
    default:
      ;
    }
  }

  final public void TypeParameters() throws ParseException {
    jj_consume_token(LT);
    TypeParameter();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_8;
      }
      jj_consume_token(COMMA);
      TypeParameter();
    }
    jj_consume_token(GT);
  }

  final public void TypeParameter() throws ParseException {
    jj_consume_token(IDENTIFIER);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXTENDS:
      TypeBound();
      break;
    default:
      ;
    }
  }

  final public void TypeBound() throws ParseException {
    jj_consume_token(EXTENDS);
    ClassOrInterfaceType();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_AND:
        ;
        break;
      default:
        break label_9;
      }
      jj_consume_token(BIT_AND);
      ClassOrInterfaceType();
    }
  }

  final public void ClassOrInterfaceBody(boolean isInterface) throws ParseException {
    jj_consume_token(LBRACE);
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ABSTRACT:
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case CLASS:
      case DOUBLE:
      case ENUM:
      case FINAL:
      case FLOAT:
      case INT:
      case INTERFACE:
      case LONG:
      case NATIVE:
      case PRIVATE:
      case PROTECTED:
      case PUBLIC:
      case SHORT:
      case STATIC:
      case STRICTFP:
      case SYNCHRONIZED:
      case TRANSIENT:
      case VOID:
      case VOLATILE:
      case IDENTIFIER:
      case LBRACE:
      case SEMICOLON:
      case AT:
      case LT:
        ;
        break;
      default:
        break label_10;
      }
      ClassOrInterfaceBodyDeclaration(isInterface);
    }
    jj_consume_token(RBRACE);
  }

  final public void ClassOrInterfaceBodyDeclaration(boolean isInterface) throws ParseException {
   int modifiers;
    if (jj_2_4(2)) {
      Initializer();
     if (isInterface)
        {if (true) throw new ParseException("An interface cannot have initializers");}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ABSTRACT:
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case CLASS:
      case DOUBLE:
      case ENUM:
      case FINAL:
      case FLOAT:
      case INT:
      case INTERFACE:
      case LONG:
      case NATIVE:
      case PRIVATE:
      case PROTECTED:
      case PUBLIC:
      case SHORT:
      case STATIC:
      case STRICTFP:
      case SYNCHRONIZED:
      case TRANSIENT:
      case VOID:
      case VOLATILE:
      case IDENTIFIER:
      case AT:
      case LT:
        modifiers = Modifiers();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CLASS:
        case INTERFACE:
          ClassOrInterfaceDeclaration(modifiers);
          break;
        case ENUM:
          EnumDeclaration(modifiers);
          break;
        default:
          if (jj_2_2(2147483647)) {
            ConstructorDeclaration();
          } else if (jj_2_3(2147483647)) {
            FieldDeclaration(modifiers);
          } else {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
            case VOID:
            case IDENTIFIER:
            case LT:
              MethodDeclaration(modifiers);
              break;
            default:
              jj_consume_token(-1);
              throw new ParseException();
            }
          }
        }
        break;
      case SEMICOLON:
        jj_consume_token(SEMICOLON);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void FieldDeclaration(int modifiers) throws ParseException {
    Type();
    VariableDeclarator();
    label_11:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_11;
      }
      jj_consume_token(COMMA);
      VariableDeclarator();
    }
    jj_consume_token(SEMICOLON);
  }

  final public void VariableDeclarator() throws ParseException {
    VariableDeclaratorId();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ASSIGN:
      jj_consume_token(ASSIGN);
      VariableInitializer();
      break;
    default:
      ;
    }
  }

  final public void VariableDeclaratorId() throws ParseException {
    jj_consume_token(IDENTIFIER);
    label_12:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACKET:
        ;
        break;
      default:
        break label_12;
      }
      jj_consume_token(LBRACKET);
      jj_consume_token(RBRACKET);
    }
  }

  final public StringBuffer VariableInitializer() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
      sb.append(ArrayInitializer());
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      sb.append(Expression());
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer ArrayInitializer() throws ParseException {
	StringBuffer sb = new StringBuffer();
    jj_consume_token(LBRACE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case LBRACE:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      sb.append(VariableInitializer());
      label_13:
      while (true) {
        if (jj_2_5(2)) {
          ;
        } else {
          break label_13;
        }
        sb.append(jj_consume_token(COMMA).image);
        sb.append(VariableInitializer());
      }
      break;
    default:
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COMMA:
      sb.append(jj_consume_token(COMMA).image);
      break;
    default:
      ;
    }
    sb.append(jj_consume_token(RBRACE).image);
    return sb;
  }

  final public void MethodDeclaration(int modifiers) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LT:
      TypeParameters();
      break;
    default:
      ;
    }
    ResultType();
    MethodDeclarator();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case THROWS:
      jj_consume_token(THROWS);
      NameList();
      break;
    default:
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
      Block();
      break;
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void MethodDeclarator() throws ParseException {
    jj_consume_token(IDENTIFIER);
    /* FlowGenerator - inicio */
    gfc.openMethodCluster(FlowGenerator.mlp.getString("method")+" "+getToken(jj_ntk)+"()");
    gfc.processCommand(BEGIN);
    gfc.addInformation("(begin)",null,jj_input_stream.line);
    /* FlowGenerator - fim */
    FormalParameters();
    label_14:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACKET:
        ;
        break;
      default:
        break label_14;
      }
      jj_consume_token(LBRACKET);
      jj_consume_token(RBRACKET);
    }
  }

  final public void FormalParameters() throws ParseException {
    jj_consume_token(LPAREN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FINAL:
    case FLOAT:
    case INT:
    case LONG:
    case SHORT:
    case IDENTIFIER:
      FormalParameter();
      label_15:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          break label_15;
        }
        jj_consume_token(COMMA);
        FormalParameter();
      }
      break;
    default:
      ;
    }
    jj_consume_token(RPAREN);
  }

  final public void FormalParameter() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FINAL:
      jj_consume_token(FINAL);
      break;
    default:
      ;
    }
    Type();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ELLIPSIS:
      jj_consume_token(ELLIPSIS);
      break;
    default:
      ;
    }
    VariableDeclaratorId();
  }

  final public void ConstructorDeclaration() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LT:
      TypeParameters();
      break;
    default:
      ;
    }
    jj_consume_token(IDENTIFIER);
    /* FlowGenerator - inicio */
    gfc.openMethodCluster(FlowGenerator.mlp.getString("constructor")+" "+getToken(jj_ntk)+"()");
    gfc.processCommand(BEGIN);
    gfc.addInformation("(begin)",null,jj_input_stream.line);
    /* FlowGenerator - fim */

    FormalParameters();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case THROWS:
      jj_consume_token(THROWS);
      NameList();
      break;
    default:
      ;
    }
    jj_consume_token(LBRACE);
    if (jj_2_6(2147483647)) {
      ExplicitConstructorInvocation();
    } else {
      ;
    }
    label_16:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSERT:
      case BOOLEAN:
      case BREAK:
      case BYTE:
      case CHAR:
      case CLASS:
      case CONTINUE:
      case DO:
      case DOUBLE:
      case FALSE:
      case FINAL:
      case FLOAT:
      case FOR:
      case IF: 
      case INT:
      case INTERFACE:
      case LONG:
      case NEW:
      case NULL:
      case RETURN:
      case SHORT:
      case SUPER:
      case SWITCH:
      case SYNCHRONIZED:
      case THIS:
      case THROW:
      case TRUE:
      case TRY:
      case VOID:
      case WHILE:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case LBRACE:
      case SEMICOLON:
      case INCR:
      case DECR:
        ;
        break;
      default:
        break label_16;
      }
      BlockStatement();
    }
    jj_consume_token(RBRACE);
  }

  final public void ExplicitConstructorInvocation() throws ParseException {
    if (jj_2_8(2147483647)) {
      jj_consume_token(THIS);
      Arguments();
      jj_consume_token(SEMICOLON);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case SUPER:
      case THIS:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
        if (jj_2_7(2)) {
          PrimaryExpression();
          jj_consume_token(DOT);
        } else {
          ;
        }
        jj_consume_token(SUPER);
        Arguments();
        jj_consume_token(SEMICOLON);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void Initializer() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STATIC:
      jj_consume_token(STATIC);
      break;
    default:
      ;
    }
    Block();
  }

/*
 * Type, name and expression syntax follows.
 */
  final public StringBuffer Type() throws ParseException {
	StringBuffer sb = new StringBuffer();
    if (jj_2_9(2)) {
      sb.append(ReferenceType());
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
        sb.append(PrimitiveType());
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    return sb;
  }

  final public StringBuffer ReferenceType() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FLOAT:
    case INT:
    case LONG:
    case SHORT:
      sb.append(PrimitiveType());
      label_17:
      while (true) {
        sb.append(jj_consume_token(LBRACKET).image);
        sb.append(jj_consume_token(RBRACKET).image);
        if (jj_2_10(2)) {
          ;
        } else {
          break label_17;
        }
      }
      break;
    case IDENTIFIER:
      sb.append(ClassOrInterfaceType());
      label_18:
      while (true) {
        if (jj_2_11(2)) {
          ;
        } else {
          break label_18;
        }
        sb.append(jj_consume_token(LBRACKET).image);
        sb.append(jj_consume_token(RBRACKET).image);
      }
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer ClassOrInterfaceType() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(IDENTIFIER).image);
    if (jj_2_12(2)) {
      sb.append(TypeArguments());
    } else {
      ;
    }
    label_19:
    while (true) {
      if (jj_2_13(2)) {
        ;
      } else {
        break label_19;
      }
      sb.append(jj_consume_token(DOT).image);
      sb.append(jj_consume_token(IDENTIFIER).image);
      if (jj_2_14(2)) {
        sb.append(TypeArguments());
      } else {
        ;
      }
    }
    return sb;
  }

  final public StringBuffer TypeArguments() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(LT).image);
    sb.append(TypeArgument());
    label_20:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_20;
      }
      sb.append(jj_consume_token(COMMA).image);
      sb.append(TypeArgument());
    }
    sb.append(jj_consume_token(GT).image);
    return sb;
  }

  final public StringBuffer TypeArgument() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FLOAT:
    case INT:
    case LONG:
    case SHORT:
    case IDENTIFIER:
      sb.append(ReferenceType());
      break;
    case HOOK:
      sb.append(jj_consume_token(HOOK).image);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EXTENDS:
      case SUPER:
        sb.append(WildcardBounds());
        break;
      default:
        ;
      }
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer WildcardBounds() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXTENDS:
      sb.append(jj_consume_token(EXTENDS).image);
      sb.append(ReferenceType());
      break;
    case SUPER:
      sb.append(jj_consume_token(SUPER).image);
      sb.append(ReferenceType());
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer PrimitiveType() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
      sb.append(jj_consume_token(BOOLEAN).image);
      break;
    case CHAR:
      sb.append(jj_consume_token(CHAR).image);
      break;
    case BYTE:
      sb.append(jj_consume_token(BYTE).image);
      break;
    case SHORT:
      sb.append(jj_consume_token(SHORT).image);
      break;
    case INT:
      sb.append(jj_consume_token(INT).image);
      break;
    case LONG:
      sb.append(jj_consume_token(LONG).image);
      break;
    case FLOAT:
      sb.append(jj_consume_token(FLOAT).image);
      break;
    case DOUBLE:
      sb.append(jj_consume_token(DOUBLE).image);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer ResultType() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VOID:
      sb.append(jj_consume_token(VOID).image);
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FLOAT:
    case INT:
    case LONG:
    case SHORT:
    case IDENTIFIER:
      sb.append(Type());
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer Name() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(IDENTIFIER).image);
    label_21:
    while (true) {
      if (jj_2_15(2)) {
        ;
      } else {
        break label_21;
      }
      sb.append(jj_consume_token(DOT).image);
      sb.append(jj_consume_token(IDENTIFIER).image);
    }
    return sb;
  }

  final public void NameList() throws ParseException {
    Name();
    label_22:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_22;
      }
      jj_consume_token(COMMA);
      Name();
    }
  }

/*
 * Expression syntax follows.
 */
  final public StringBuffer Expression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(ConditionalExpression());
    if (jj_2_16(2)) {
      sb.append(AssignmentOperator());
      sb.append(Expression());
    } else {
      ;
    }
    return sb;
  }

  final public StringBuffer AssignmentOperator() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ASSIGN:
      sb.append(jj_consume_token(ASSIGN).image);
      break;
    case STARASSIGN:
      sb.append(jj_consume_token(STARASSIGN).image);
      break;
    case SLASHASSIGN:
      sb.append(jj_consume_token(SLASHASSIGN).image);
      break;
    case REMASSIGN:
      sb.append(jj_consume_token(REMASSIGN).image);
      break;
    case PLUSASSIGN:
      sb.append(jj_consume_token(PLUSASSIGN).image);
      break;
    case MINUSASSIGN:
      sb.append(jj_consume_token(MINUSASSIGN).image);
      break;
    case LSHIFTASSIGN:
      sb.append(jj_consume_token(LSHIFTASSIGN).image);
      break;
    case RSIGNEDSHIFTASSIGN:
      sb.append(jj_consume_token(RSIGNEDSHIFTASSIGN).image);
      break;
    case RUNSIGNEDSHIFTASSIGN:
      sb.append(jj_consume_token(RUNSIGNEDSHIFTASSIGN).image);
      break;
    case ANDASSIGN:
      sb.append(jj_consume_token(ANDASSIGN).image);
      break;
    case XORASSIGN:
      sb.append(jj_consume_token(XORASSIGN).image);
      break;
    case ORASSIGN:
      sb.append(jj_consume_token(ORASSIGN).image);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer ConditionalExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(ConditionalOrExpression());
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case HOOK:
      sb.append(jj_consume_token(HOOK).image);
      sb.append(Expression());
      sb.append(jj_consume_token(COLON).image);
      sb.append(Expression());
      break;
    default:
      ;
    }
    return sb;
  }

  final public StringBuffer ConditionalOrExpression() throws ParseException {
    StringBuffer sb = new StringBuffer();
    sb.append(ConditionalAndExpression());
    label_23:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SC_OR:
        ;
        break;
      default:
        break label_23;
      }
      sb.append(jj_consume_token(SC_OR).image);
      sb.append(ConditionalAndExpression());
    }
    return sb;
  }

  final public StringBuffer ConditionalAndExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(InclusiveOrExpression());
    label_24:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SC_AND:
        ;
        break;
      default:
        break label_24;
      }
      sb.append(jj_consume_token(SC_AND).image);
      sb.append(InclusiveOrExpression());
    }
    return sb;
  }

  final public StringBuffer InclusiveOrExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(ExclusiveOrExpression());
    label_25:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_OR:
        ;
        break;
      default:
        break label_25;
      }
      sb.append(jj_consume_token(BIT_OR).image);
      sb.append(ExclusiveOrExpression());
    }
    return sb;
  }

  final public StringBuffer ExclusiveOrExpression() throws ParseException {
    StringBuffer sb = new StringBuffer();
    sb.append(AndExpression());
    label_26:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case XOR:
        ;
        break;
      default:
        break label_26;
      }
      sb.append(jj_consume_token(XOR).image);
      sb.append(AndExpression());
    }
    return sb;
  }

  final public StringBuffer AndExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(EqualityExpression());
    label_27:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BIT_AND:
        ;
        break;
      default:
        break label_27;
      }
      sb.append(jj_consume_token(BIT_AND).image);
      sb.append(EqualityExpression());
    }
    return sb;
  }

  final public StringBuffer EqualityExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(InstanceOfExpression());
    label_28:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
      case NE:
        ;
        break;
      default:
        break label_28;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
    	  sb.append(jj_consume_token(EQ).image);
        break;
      case NE:
    	  sb.append(jj_consume_token(NE).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      sb.append(InstanceOfExpression());
    }
    return sb;
  }

  final public StringBuffer InstanceOfExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(RelationalExpression());
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INSTANCEOF:
      sb.append(jj_consume_token(INSTANCEOF).image);
      // Falta verificar
      sb.append(Type());
      break;
    default:
      ;
    }
    return sb;
  }

  final public StringBuffer RelationalExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(ShiftExpression());
    label_29:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LT:
      case LE:
      case GE:
      case GT:
        ;
        break;
      default:
        break label_29;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LT:
        sb.append(jj_consume_token(LT).image);
        break;
      case GT:
        sb.append(jj_consume_token(GT).image);
        break;
      case LE:
        sb.append(jj_consume_token(LE).image);
        break;
      case GE:
        sb.append(jj_consume_token(GE).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      sb.append(ShiftExpression());
    }
    return sb;
  }

  final public StringBuffer ShiftExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(AdditiveExpression());
    label_30:
    while (true) {
      if (jj_2_17(1)) {
        ;
      } else {
        break label_30;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LSHIFT:
        sb.append(jj_consume_token(LSHIFT).image);
        break;
      default:
        if (jj_2_18(1)) {
        	sb.append(RSIGNEDSHIFT());
        } else if (jj_2_19(1)) {
        	sb.append(RUNSIGNEDSHIFT());
        } else {
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      sb.append(AdditiveExpression());
    }
    return sb;
  }

  final public StringBuffer AdditiveExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(MultiplicativeExpression());
    label_31:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
      case MINUS:
        ;
        break;
      default:
        break label_31;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        sb.append(jj_consume_token(PLUS).image);
        break;
      case MINUS:
        sb.append(jj_consume_token(MINUS).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      sb.append(MultiplicativeExpression());
    }
    return sb;
  }

  final public StringBuffer MultiplicativeExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(UnaryExpression());
    label_32:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
      case SLASH:
      case REM:
        ;
        break;
      default:
        break label_32;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case STAR:
        sb.append(jj_consume_token(STAR).image);
        break;
      case SLASH:
        sb.append(jj_consume_token(SLASH).image);
        break;
      case REM:
        sb.append(jj_consume_token(REM).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      sb.append(UnaryExpression());
    }
    return sb;
  }

  final public StringBuffer UnaryExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
    case MINUS:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PLUS:
        sb.append(jj_consume_token(PLUS).image);
        break;
      case MINUS:
        sb.append(jj_consume_token(MINUS).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      sb.append(UnaryExpression());
      break;
    case INCR:
      sb.append(PreIncrementExpression());
      break;
    case DECR:
      sb.append(PreDecrementExpression());
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
      sb.append(UnaryExpressionNotPlusMinus());
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer PreIncrementExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(INCR).image);
    sb.append(PrimaryExpression());
    return sb;
  }

  final public StringBuffer PreDecrementExpression() throws ParseException {
    StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(DECR).image);
    sb.append(PrimaryExpression());
    return sb;
  }

  final public StringBuffer UnaryExpressionNotPlusMinus() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BANG:
    case TILDE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TILDE:
        sb.append(jj_consume_token(TILDE).image);
        break;
      case BANG:
        sb.append(jj_consume_token(BANG).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      sb.append(UnaryExpression());
      break;
    default:
      if (jj_2_20(2147483647)) {
        sb.append(CastExpression());
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FALSE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case SHORT:
        case SUPER:
        case THIS:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
          sb.append(PostfixExpression());
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
    return sb;
  }

// This production is to determine lookahead only.  The LOOKAHEAD specifications
// below are not used, but they are there just to indicate that we know about
// this.
  final public void CastLookahead() throws ParseException {
    if (jj_2_21(2)) {
      jj_consume_token(LPAREN);
      PrimitiveType();
    } else if (jj_2_22(2147483647)) {
      jj_consume_token(LPAREN);
      Type();
      jj_consume_token(LBRACKET);
      jj_consume_token(RBRACKET);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        Type();
        jj_consume_token(RPAREN);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TILDE:
          jj_consume_token(TILDE);
          break;
        case BANG:
          jj_consume_token(BANG);
          break;
        case LPAREN:
          jj_consume_token(LPAREN);
          break;
        case IDENTIFIER:
          jj_consume_token(IDENTIFIER);
          break;
        case THIS:
          jj_consume_token(THIS);
          break;
        case SUPER:
          jj_consume_token(SUPER);
          break;
        case NEW:
          jj_consume_token(NEW);
          break;
        case FALSE:
        case NULL:
        case TRUE:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
          Literal();
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public StringBuffer PostfixExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(PrimaryExpression());
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INCR:
    case DECR:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INCR:
        sb.append(jj_consume_token(INCR).image);
        break;
      case DECR:
        sb.append(jj_consume_token(DECR).image);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      ;
    }
    return sb;
  }

  final public StringBuffer CastExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    if (jj_2_23(2147483647)) {
      sb.append(jj_consume_token(LPAREN).image);
      Type();
      sb.append(jj_consume_token(RPAREN).image);
      sb.append(UnaryExpression());
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        sb.append(jj_consume_token(LPAREN).image);
        sb.append(Type());
        sb.append(jj_consume_token(RPAREN).image);
        sb.append(UnaryExpressionNotPlusMinus());
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    return sb;
  }

  final public StringBuffer PrimaryExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(PrimaryPrefix());
    label_33:
    while (true) {
      if (jj_2_24(2)) {
        ;
      } else {
        break label_33;
      }
      sb.append(PrimarySuffix());
    }
    return sb;
  }

  final public StringBuffer MemberSelector() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(DOT).image);
    sb.append(TypeArguments());
    sb.append(jj_consume_token(IDENTIFIER).image);
    return sb;
  }

  final public StringBuffer PrimaryPrefix() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FALSE:
    case NULL:
    case TRUE:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
      sb.append(Literal());
      break;
    case THIS:
      sb.append(jj_consume_token(THIS).image);
      break;
    case SUPER:
      sb.append(jj_consume_token(SUPER).image);
      sb.append(jj_consume_token(DOT).image);
      sb.append(jj_consume_token(IDENTIFIER).image);
      break;
    case LPAREN:
      sb.append(jj_consume_token(LPAREN).image);
      sb.append(Expression());
      sb.append(jj_consume_token(RPAREN));
      break;
    case NEW:
      sb.append(AllocationExpression());
      break;
    default:
      if (jj_2_25(2147483647)) {
        sb.append(ResultType());
        sb.append(jj_consume_token(DOT).image);
        sb.append(jj_consume_token(CLASS).image);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IDENTIFIER:
          sb.append(Name());
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
    return sb;
  }

  final public StringBuffer PrimarySuffix() throws ParseException {
	StringBuffer sb = new StringBuffer();
    if (jj_2_26(2)) {
      sb.append(jj_consume_token(DOT).image);
      sb.append(jj_consume_token(THIS).image);
    } else if (jj_2_27(2)) {
      sb.append(jj_consume_token(DOT).image);
      sb.append(AllocationExpression());
    } else if (jj_2_28(3)) {
      sb.append(MemberSelector());
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACKET:
        sb.append(jj_consume_token(LBRACKET).image);
        sb.append(Expression());
        sb.append(jj_consume_token(RBRACKET).image);
        break;
      case DOT:
        sb.append(jj_consume_token(DOT).image);
        sb.append(jj_consume_token(IDENTIFIER).image);
        break;
      case LPAREN:
        sb.append(Arguments());
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    return sb;
  }

  final public StringBuffer Literal() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
      sb.append(jj_consume_token(INTEGER_LITERAL).image);
      break;
    case FLOATING_POINT_LITERAL:
      sb.append(jj_consume_token(FLOATING_POINT_LITERAL).image);
      break;
    case CHARACTER_LITERAL:
      sb.append(jj_consume_token(CHARACTER_LITERAL).image);
      break;
    case STRING_LITERAL:
      sb.append(jj_consume_token(STRING_LITERAL).image);
      break;
    case FALSE:
    case TRUE:
      sb.append(BooleanLiteral());
      break;
    case NULL:
      // Falta verificar
      NullLiteral();
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public StringBuffer BooleanLiteral() throws ParseException {
	StringBuffer sb = new StringBuffer();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      sb.append(jj_consume_token(TRUE).image);
      break;
    case FALSE:
      sb.append(jj_consume_token(FALSE).image);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
    return sb;
  }

  final public void NullLiteral() throws ParseException {
    jj_consume_token(NULL);
  }

  final public StringBuffer Arguments() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(jj_consume_token(LPAREN).image);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      sb.append(ArgumentList());
      break;
    default:
      ;
    }
    sb.append(jj_consume_token(RPAREN).image);
    return sb;
  }

  final public StringBuffer ArgumentList() throws ParseException {
	StringBuffer sb = new StringBuffer();
    sb.append(Expression());
    label_34:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_34;
      }
      sb.append(jj_consume_token(COMMA).image);
      sb.append(Expression());
    }
    return sb;
  }

  final public StringBuffer AllocationExpression() throws ParseException {
	StringBuffer sb = new StringBuffer();
    if (jj_2_29(2)) {
      sb.append(jj_consume_token(NEW).image);
      sb.append(PrimitiveType());
      sb.append(ArrayDimsAndInits());
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NEW:
        sb.append(jj_consume_token(NEW).image);
        sb.append(ClassOrInterfaceType());
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LT:
          sb.append(TypeArguments());
          break;
        default:
          ;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACKET:
          sb.append(ArrayDimsAndInits());
          break;
        case LPAREN:
          sb.append(Arguments());
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case LBRACE:
        	//Falta verificar
            ClassOrInterfaceBody(false);
            break;
          default:
            ;
          }
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    return sb;
  }

/*
 * The third LOOKAHEAD specification below is to parse to PrimarySuffix
 * if there is an expression between the "[...]".
 */
  final public StringBuffer ArrayDimsAndInits() throws ParseException {
	StringBuffer sb = new StringBuffer();  
    if (jj_2_32(2)) {
      label_35:
      while (true) {
        sb.append(jj_consume_token(LBRACKET).image);
        sb.append(Expression());
        sb.append(jj_consume_token(RBRACKET).image);
        if (jj_2_30(2)) {
          ;
        } else {
          break label_35;
        }
      }
      label_36:
      while (true) {
        if (jj_2_31(2)) {
          ;
        } else {
          break label_36;
        }
        sb.append(jj_consume_token(LBRACKET).image);
        sb.append(jj_consume_token(RBRACKET).image);
      }
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACKET:
        label_37:
        while (true) {
          sb.append(jj_consume_token(LBRACKET).image);
          sb.append(jj_consume_token(RBRACKET).image);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case LBRACKET:
            ;
            break;
          default:
            break label_37;
          }
        }
        sb.append(ArrayInitializer());
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    return sb;
  }

/*
 * Statement syntax follows.
 */
  final public void Statement() throws ParseException {
    if (jj_2_33(2)) {
      LabeledStatement();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSERT:
        AssertStatement();
        break;
      case LBRACE:
        Block();
        break;
      case SEMICOLON:
        EmptyStatement();
        break;
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case SUPER:
      case THIS:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case INCR:
      case DECR:
        StatementExpression();
        jj_consume_token(SEMICOLON);
        break;
      case SWITCH: 
        /* FlowGenerator - inicio */ 
        gfc.processCommand(SWITCH);
        /* FlowGenerator - fim */
        SwitchStatement();
        /* FlowGenerator - inicio */ 
        gfc.processCommand(ENDCASE);
        gfc.addInformation("(end-case)",null,jj_input_stream.line);
        /* FlowGenerator - fim */
        break;
      case IF: 
        /* FlowGenerator - inicio */ 

        gfc.processCommand(IF);
        /* FlowGenerator - fim */
        IfStatement();
        /* FlowGenerator - inicio */ 
        gfc.processCommand(ENDIF);
        gfc.addInformation("(end-if)",null,jj_input_stream.line);
        /* FlowGenerator - fim */
        break;
      case WHILE:
        /* FlowGenerator - inicio */ 
        gfc.processCommand(WHILE);
        /* FlowGenerator - fim */
        WhileStatement();
        /* FlowGenerator - inicio */ 
        gfc.processCommand(LOOP);
        gfc.addInformation("(loop)",null,jj_input_stream.line);
        /* FlowGenerator - fim */
        break;
      case DO:
        /* FlowGenerator - inicio */ 
        gfc.processCommand(DO);
        /* FlowGenerator - fim */      
        DoStatement();
        /* FlowGenerator - inicio */ 
        gfc.processCommand(ENDDO);
        gfc.addInformation("(end-do)",null,jj_input_stream.line);
        /* FlowGenerator - fim */
        break;
      case FOR: 
        /* FlowGenerator - inicio */ 
        gfc.processCommand(FOR);
        /* FlowGenerator - fim */      
        ForStatement();
        /* FlowGenerator - inicio */ 
        gfc.processCommand(ENDFOR);
        gfc.addInformation("(end-for)",null,jj_input_stream.line);
        /* FlowGenerator - fim */
        break;
      case BREAK:
        BreakStatement();
        break;
      case CONTINUE:
        ContinueStatement();
        break;
      case RETURN:
        ReturnStatement();
        break;
      case THROW:
        ThrowStatement();
        break;
      case SYNCHRONIZED:
        SynchronizedStatement();
        break;
      case TRY:
        TryStatement();
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void AssertStatement() throws ParseException {
    jj_consume_token(ASSERT);
    Expression();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COLON:
      jj_consume_token(COLON);
      Expression();
      break;
    default:
      ;
    }
    jj_consume_token(SEMICOLON);
  }

  final public void LabeledStatement() throws ParseException {
    jj_consume_token(IDENTIFIER);
    jj_consume_token(COLON);
    Statement();
  }

  final public void Block() throws ParseException {
    jj_consume_token(LBRACE);
    label_38:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSERT:
      case BOOLEAN:
      case BREAK:
      case BYTE:
      case CHAR:
      case CLASS:
      case CONTINUE:
      case DO:
      case DOUBLE:
      case FALSE:
      case FINAL:
      case FLOAT:
      case FOR:
      case IF:
      case INT:
      case INTERFACE:
      case LONG:
      case NEW:
      case NULL:
      case RETURN:
      case SHORT:
      case SUPER:
      case SWITCH:
      case SYNCHRONIZED:
      case THIS:
      case THROW:
      case TRUE:
      case TRY:
      case VOID:
      case WHILE:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case LBRACE:
      case SEMICOLON:
      case INCR:
      case DECR:
        ;
        break;
      default:
        break label_38;
      }
      BlockStatement();
    }
    jj_consume_token(RBRACE);
  }

  final public void BlockStatement() throws ParseException {
    if (jj_2_34(2147483647)) {
      LocalVariableDeclaration();
      jj_consume_token(SEMICOLON);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSERT:
      case BOOLEAN:
      case BREAK:
      case BYTE:
      case CHAR:
      case CONTINUE:
      case DO:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case FOR:
      case IF:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case RETURN:
      case SHORT:
      case SUPER:
      case SWITCH:
      case SYNCHRONIZED:
      case THIS:
      case THROW:
      case TRUE:
      case TRY:
      case VOID:
      case WHILE:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case LBRACE:
      case SEMICOLON:
      case INCR:
      case DECR:
        Statement();
        break;
      case CLASS:
      case INTERFACE:
        ClassOrInterfaceDeclaration(0);
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void LocalVariableDeclaration() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FINAL:
      jj_consume_token(FINAL);
      break;
    default:
      ;
    }
    Type();
    VariableDeclarator();
    label_39:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_39;
      }
      jj_consume_token(COMMA);
      VariableDeclarator();
    }
  }

  final public void EmptyStatement() throws ParseException {
    jj_consume_token(SEMICOLON);
  }

  final public void StatementExpression() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INCR:
      PreIncrementExpression();
      break;
    case DECR:
      PreDecrementExpression();
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
      PrimaryExpression();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSIGN:
      case INCR:
      case DECR:
      case PLUSASSIGN:
      case MINUSASSIGN:
      case STARASSIGN:
      case SLASHASSIGN:
      case ANDASSIGN:
      case ORASSIGN:
      case XORASSIGN:
      case REMASSIGN:
      case LSHIFTASSIGN:
      case RSIGNEDSHIFTASSIGN:
      case RUNSIGNEDSHIFTASSIGN:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INCR:
          jj_consume_token(INCR);
          break;
        case DECR:
          jj_consume_token(DECR);
          break;
        case ASSIGN:
        case PLUSASSIGN:
        case MINUSASSIGN:
        case STARASSIGN:
        case SLASHASSIGN:
        case ANDASSIGN:
        case ORASSIGN:
        case XORASSIGN:
        case REMASSIGN:
        case LSHIFTASSIGN:
        case RSIGNEDSHIFTASSIGN:
        case RUNSIGNEDSHIFTASSIGN:
          AssignmentOperator();
          Expression();
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        ;
      }
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void SwitchStatement() throws ParseException {
    String expr = new String();
	jj_consume_token(SWITCH);
    jj_consume_token(LPAREN);
    expr = Expression().toString();
    gfc.addInformation("switch",expr,jj_input_stream.line);
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    label_40:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CASE:
      case _DEFAULT:
        ;
        break;
      default:
        break label_40;
      }
      SwitchLabel();
      label_41:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ASSERT:
        case BOOLEAN:
        case BREAK:
        case BYTE:
        case CHAR:
        case CLASS:
        case CONTINUE:
        case DO:
        case DOUBLE:
        case FALSE:
        case FINAL:
        case FLOAT:
        case FOR:
        case IF:
        case INT:
        case INTERFACE:
        case LONG:
        case NEW:
        case NULL:
        case RETURN:
        case SHORT:
        case SUPER:
        case SWITCH:
        case SYNCHRONIZED:
        case THIS:
        case THROW:
        case TRUE:
        case TRY:
        case VOID:
        case WHILE:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
        case LBRACE:
        case SEMICOLON:
        case INCR:
        case DECR:
          ;
          break;
        default:
          break label_41;
        }
        BlockStatement();
      }
    }
    jj_consume_token(RBRACE);
  }

  final public void SwitchLabel() throws ParseException {
	String expr = new String();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CASE:
      jj_consume_token(CASE);
      /* FlowGenerator - inicio */
      gfc.processCommand(CASE);
      /* FlowGenerator - fim */
      expr = Expression().toString();
      gfc.addInformation("case",expr,jj_input_stream.line);
      jj_consume_token(COLON);

      break;
    case _DEFAULT:
      jj_consume_token(_DEFAULT);
      /* FlowGenerator - inicio */
      gfc.processCommand(_DEFAULT);
      gfc.addInformation("default",null,jj_input_stream.line);
      /* FlowGenerator - fim */
      jj_consume_token(COLON);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void IfStatement() throws ParseException {
    jj_consume_token(IF);
    jj_consume_token(LPAREN);
    /* FlowGenerator - inicio */
    String expr = new String();
    expr = Expression().toString();
    gfc.addInformation("if",expr,jj_input_stream.line);
    expr =null;
    /* FlowGenerator - inicio */
    jj_consume_token(RPAREN);
    Statement();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ELSE:
      jj_consume_token(ELSE);
      /* FlowGenerator - inicio */
      gfc.processCommand(ELSE);
      gfc.addInformation("else",null,jj_input_stream.line);
      /* FlowGenerator - fim */
      Statement();
      break;
    default:
    }
  }

  final public void WhileStatement() throws ParseException {
    jj_consume_token(WHILE);
    jj_consume_token(LPAREN);
    /* FlowGenerator - inicio */
    String expr = new String();
    expr = Expression().toString();
    gfc.addInformation("while",expr,jj_input_stream.line);
    /* FlowGenerator - inicio */
    jj_consume_token(RPAREN);
    Statement();
  }

  final public void DoStatement() throws ParseException {
    jj_consume_token(DO);
    Statement();
    jj_consume_token(WHILE);
    jj_consume_token(LPAREN);
    /* FlowGenerator - inicio */
    String expr = new String();
    expr = Expression().toString();
    gfc.addInformation("do-while",expr,jj_input_stream.line);
    /* FlowGenerator - inicio */
    jj_consume_token(RPAREN);
    jj_consume_token(SEMICOLON);
  }

  final public void ForStatement() throws ParseException {
    StringBuffer sb = new StringBuffer();
    jj_consume_token(FOR);
    jj_consume_token(LPAREN);
    if (jj_2_35(2147483647)) {
      sb.append(Type());
      jj_consume_token(IDENTIFIER);
      jj_consume_token(COLON);
      sb.append(Expression());
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FINAL:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case SUPER:
      case THIS:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case SEMICOLON:
      case INCR:
      case DECR:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FALSE:
        case FINAL:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case SHORT:
        case SUPER:
        case THIS:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
        case INCR:
        case DECR:
          ForInit();
          break;
        default:
          ;
        }
        jj_consume_token(SEMICOLON);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FALSE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case SHORT:
        case SUPER:
        case THIS:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
        case BANG:
        case TILDE:
        case INCR:
        case DECR:
        case PLUS:
        case MINUS:
          sb.append(Expression());
          break;
        default:
          ;
        }
        jj_consume_token(SEMICOLON);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FALSE:
        case FLOAT:
        case INT:
        case LONG:
        case NEW:
        case NULL:
        case SHORT:
        case SUPER:
        case THIS:
        case TRUE:
        case VOID:
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case LPAREN:
        case INCR:
        case DECR:
          ForUpdate();
          break;
        default:
          ;
        }
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(RPAREN);  
    gfc.addInformation("for",sb.toString(),jj_input_stream.line);
    Statement();
  }

  final public void ForInit() throws ParseException {
    if (jj_2_36(2147483647)) {
      LocalVariableDeclaration();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case DOUBLE:
      case FALSE:
      case FLOAT:
      case INT:
      case LONG:
      case NEW:
      case NULL:
      case SHORT:
      case SUPER:
      case THIS:
      case TRUE:
      case VOID:
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case CHARACTER_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case INCR:
      case DECR:
        StatementExpressionList();
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void StatementExpressionList() throws ParseException {
    StatementExpression();
    label_42:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_42;
      }
      jj_consume_token(COMMA);
      StatementExpression();
    }
  }

  final public void ForUpdate() throws ParseException {
    StatementExpressionList();
  }

  final public void BreakStatement() throws ParseException {
    jj_consume_token(BREAK);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      jj_consume_token(IDENTIFIER);
      break;
    default:
      ;
    }
    jj_consume_token(SEMICOLON);
  }

  final public void ContinueStatement() throws ParseException {
    jj_consume_token(CONTINUE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      jj_consume_token(IDENTIFIER);
      break;
    default:
      ;
    }
    jj_consume_token(SEMICOLON);
  }

  final public void ReturnStatement() throws ParseException {
    jj_consume_token(RETURN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      Expression();
      break;
    default:
      ;
    }
    jj_consume_token(SEMICOLON);
  }

  final public void ThrowStatement() throws ParseException {
    jj_consume_token(THROW);
    Expression();
    jj_consume_token(SEMICOLON);
  }

  final public void SynchronizedStatement() throws ParseException {
    jj_consume_token(SYNCHRONIZED);
    jj_consume_token(LPAREN);
    Expression();
    jj_consume_token(RPAREN);
    Block();
  }

  final public void TryStatement() throws ParseException {
    jj_consume_token(TRY);
    Block();
    label_43:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CATCH:
        ;
        break;
      default:
        break label_43;
      }
      jj_consume_token(CATCH);
      jj_consume_token(LPAREN);
      FormalParameter();
      jj_consume_token(RPAREN);
      Block();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FINALLY:
      jj_consume_token(FINALLY);
      Block();
      break;
    default:
      ;
    }
  }

/* We use productions to match >>>, >> and > so that we can keep the
 * type declaration syntax with generics clean
 */
  final public StringBuffer RUNSIGNEDSHIFT() throws ParseException {
	StringBuffer sb = new StringBuffer();
    if (getToken(1).kind == GT &&
                    ((Token.GTToken)getToken(1)).realKind == RUNSIGNEDSHIFT) {

    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    sb.append(jj_consume_token(GT).image);
    sb.append(jj_consume_token(GT).image);
    sb.append(jj_consume_token(GT).image);
    return sb;
  }

  final public StringBuffer RSIGNEDSHIFT() throws ParseException {
	StringBuffer sb = new StringBuffer();
    if (getToken(1).kind == GT &&
                    ((Token.GTToken)getToken(1)).realKind == RSIGNEDSHIFT) {

    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    sb.append(jj_consume_token(GT).image);
    sb.append(jj_consume_token(GT).image);
    return sb;
  }

/* Annotation syntax follows. */
  final public void Annotation() throws ParseException {
    if (jj_2_37(2147483647)) {
      NormalAnnotation();
    } else if (jj_2_38(2147483647)) {
      SingleMemberAnnotation();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AT:
        MarkerAnnotation();
        break;
      default:
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void NormalAnnotation() throws ParseException {
    jj_consume_token(AT);
    Name();
    jj_consume_token(LPAREN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      MemberValuePairs();
      break;
    default:
      ;
    }
    jj_consume_token(RPAREN);
  }

  final public void MarkerAnnotation() throws ParseException {
    jj_consume_token(AT);
    Name();
  }

  final public void SingleMemberAnnotation() throws ParseException {
    jj_consume_token(AT);
    Name();
    jj_consume_token(LPAREN);
    MemberValue();
    jj_consume_token(RPAREN);
  }

  final public void MemberValuePairs() throws ParseException {
    MemberValuePair();
    label_44:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        break label_44;
      }
      jj_consume_token(COMMA);
      MemberValuePair();
    }
  }

  final public void MemberValuePair() throws ParseException {
    jj_consume_token(IDENTIFIER);
    jj_consume_token(ASSIGN);
    MemberValue();
  }

  final public void MemberValue() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AT:
      Annotation();
      break;
    case LBRACE:
      MemberValueArrayInitializer();
      break;
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case DOUBLE:
    case FALSE:
    case FLOAT:
    case INT:
    case LONG:
    case NEW:
    case NULL:
    case SHORT:
    case SUPER:
    case THIS:
    case TRUE:
    case VOID:
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case CHARACTER_LITERAL:
    case STRING_LITERAL:
    case IDENTIFIER:
    case LPAREN:
    case BANG:
    case TILDE:
    case INCR:
    case DECR:
    case PLUS:
    case MINUS:
      ConditionalExpression();
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void MemberValueArrayInitializer() throws ParseException {
    jj_consume_token(LBRACE);
    MemberValue();
    label_45:
    while (true) {
      if (jj_2_39(2)) {
        ;
      } else {
        break label_45;
      }
      jj_consume_token(COMMA);
      MemberValue();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COMMA:
      jj_consume_token(COMMA);
      break;
    default:
      ;
    }
    jj_consume_token(RBRACE);
  }

/* Annotation Types. */
  final public void AnnotationTypeDeclaration(int modifiers) throws ParseException {
    jj_consume_token(AT);
    jj_consume_token(INTERFACE);
    jj_consume_token(IDENTIFIER);
    AnnotationTypeBody();
  }

  final public void AnnotationTypeBody() throws ParseException {
    jj_consume_token(LBRACE);
    label_46:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ABSTRACT:
      case BOOLEAN:
      case BYTE:
      case CHAR:
      case CLASS:
      case DOUBLE:
      case ENUM:
      case FINAL:
      case FLOAT:
      case INT:
      case INTERFACE:
      case LONG:
      case NATIVE:
      case PRIVATE:
      case PROTECTED:
      case PUBLIC:
      case SHORT:
      case STATIC:
      case STRICTFP:
      case SYNCHRONIZED:
      case TRANSIENT:
      case VOLATILE:
      case IDENTIFIER:
      case SEMICOLON:
      case AT:
        ;
        break;
      default:
        break label_46;
      }
      AnnotationTypeMemberDeclaration();
    }
    jj_consume_token(RBRACE);
  }

  final public void AnnotationTypeMemberDeclaration() throws ParseException {
   int modifiers;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ABSTRACT:
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case CLASS:
    case DOUBLE:
    case ENUM:
    case FINAL:
    case FLOAT:
    case INT:
    case INTERFACE:
    case LONG:
    case NATIVE:
    case PRIVATE:
    case PROTECTED:
    case PUBLIC:
    case SHORT:
    case STATIC:
    case STRICTFP:
    case SYNCHRONIZED:
    case TRANSIENT:
    case VOLATILE:
    case IDENTIFIER:
    case AT:
      modifiers = Modifiers();
      if (jj_2_40(2147483647)) {
        Type();
        jj_consume_token(IDENTIFIER);
        jj_consume_token(LPAREN);
        jj_consume_token(RPAREN);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case _DEFAULT:
          DefaultValue();
          break;
        default:
          ;
        }
        jj_consume_token(SEMICOLON);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case CLASS:
        case INTERFACE:
          ClassOrInterfaceDeclaration(modifiers);
          break;
        case ENUM:
          EnumDeclaration(modifiers);
          break;
        case AT:
          AnnotationTypeDeclaration(modifiers);
          break;
        case BOOLEAN:
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case IDENTIFIER:
          FieldDeclaration(modifiers);
          break;
        default:
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    default:
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void DefaultValue() throws ParseException {
    jj_consume_token(_DEFAULT);
    MemberValue();
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_11(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_11(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_12(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_12(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_13(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_13(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_14(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_14(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_15(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_15(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_16(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_16(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_17(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_17(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_18(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_18(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_19(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_19(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_20(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_20(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_21(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_21(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_22(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_22(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_23(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_23(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_24(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_24(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_25(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_25(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_26(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_26(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_27(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_27(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_28(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_28(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_29(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_29(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_30(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_30(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_31(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_31(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_32(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_32(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_33(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_33(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_34(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_34(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_35(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_35(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_36(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_36(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_37(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_37(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_38(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_38(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_39(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_39(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_2_40(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_40(); }
    catch(LookaheadSuccess ls) { return true; }
  }

  final private boolean jj_3R_94() {
    if (jj_3R_123()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_11()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_93() {
    if (jj_3R_74()) return true;
    Token xsp;
    if (jj_3_10()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_10()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_67() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_93()) {
    jj_scanpos = xsp;
    if (jj_3R_94()) return true;
    }
    return false;
  }

  final private boolean jj_3R_239() {
    if (jj_scan_token(THROWS)) return true;
    if (jj_3R_258()) return true;
    return false;
  }

  final private boolean jj_3R_85() {
    if (jj_3R_74()) return true;
    return false;
  }

  final private boolean jj_3_9() {
    if (jj_3R_67()) return true;
    return false;
  }

  final private boolean jj_3R_60() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_9()) {
    jj_scanpos = xsp;
    if (jj_3R_85()) return true;
    }
    return false;
  }

  final private boolean jj_3_8() {
    if (jj_scan_token(THIS)) return true;
    if (jj_3R_66()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_62() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(52)) jj_scanpos = xsp;
    if (jj_3R_86()) return true;
    return false;
  }

  final private boolean jj_3_6() {
    if (jj_3R_64()) return true;
    return false;
  }

  final private boolean jj_3_7() {
    if (jj_3R_65()) return true;
    if (jj_scan_token(DOT)) return true;
    return false;
  }

  final private boolean jj_3R_261() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_90() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_7()) jj_scanpos = xsp;
    if (jj_scan_token(SUPER)) return true;
    if (jj_3R_66()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_270() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_269()) return true;
    return false;
  }

  final private boolean jj_3R_89() {
    if (jj_scan_token(THIS)) return true;
    if (jj_3R_66()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_64() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_89()) {
    jj_scanpos = xsp;
    if (jj_3R_90()) return true;
    }
    return false;
  }

  final private boolean jj_3R_241() {
    if (jj_3R_133()) return true;
    return false;
  }

  final private boolean jj_3R_240() {
    if (jj_3R_64()) return true;
    return false;
  }

  final private boolean jj_3R_237() {
    if (jj_3R_84()) return true;
    return false;
  }

  final private boolean jj_3R_227() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_237()) jj_scanpos = xsp;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_3R_238()) return true;
    xsp = jj_scanpos;
    if (jj_3R_239()) jj_scanpos = xsp;
    if (jj_scan_token(LBRACE)) return true;
    xsp = jj_scanpos;
    if (jj_3R_240()) jj_scanpos = xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_241()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3_5() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_63()) return true;
    return false;
  }

  final private boolean jj_3R_246() {
    if (jj_scan_token(THROWS)) return true;
    if (jj_3R_258()) return true;
    return false;
  }

  final private boolean jj_3R_269() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(31)) jj_scanpos = xsp;
    if (jj_3R_60()) return true;
    xsp = jj_scanpos;
    if (jj_scan_token(121)) jj_scanpos = xsp;
    if (jj_3R_259()) return true;
    return false;
  }

  final private boolean jj_3R_257() {
    if (jj_3R_269()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_270()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_238() {
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_257()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_245() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_3R_238()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_261()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_247() {
    if (jj_3R_86()) return true;
    return false;
  }

  final private boolean jj_3_40() {
    if (jj_3R_60()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_244() {
    if (jj_3R_84()) return true;
    return false;
  }

  final private boolean jj_3R_229() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_244()) jj_scanpos = xsp;
    if (jj_3R_76()) return true;
    if (jj_3R_245()) return true;
    xsp = jj_scanpos;
    if (jj_3R_246()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_247()) {
    jj_scanpos = xsp;
    if (jj_scan_token(83)) return true;
    }
    return false;
  }

  final private boolean jj_3R_206() {
    if (jj_3R_63()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_5()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_260() {
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_3R_63()) return true;
    return false;
  }

  final private boolean jj_3R_243() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_242()) return true;
    return false;
  }

  final private boolean jj_3R_272() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_115() {
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_206()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(84)) jj_scanpos = xsp;
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_61() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3_39() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_82()) return true;
    return false;
  }

  final private boolean jj_3R_88() {
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_87() {
    if (jj_3R_115()) return true;
    return false;
  }

  final private boolean jj_3R_63() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_87()) {
    jj_scanpos = xsp;
    if (jj_3R_88()) return true;
    }
    return false;
  }

  final private boolean jj_3R_259() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_272()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_242() {
    if (jj_3R_259()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_260()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3_3() {
    if (jj_3R_60()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_61()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_scan_token(84)) {
    jj_scanpos = xsp;
    if (jj_scan_token(87)) {
    jj_scanpos = xsp;
    if (jj_scan_token(83)) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_59() {
    if (jj_3R_84()) return true;
    return false;
  }

  final private boolean jj_3R_128() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_3R_82()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_39()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_scan_token(84)) jj_scanpos = xsp;
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_288() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_287()) return true;
    return false;
  }

  final private boolean jj_3R_228() {
    if (jj_3R_60()) return true;
    if (jj_3R_242()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_243()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3_2() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_59()) jj_scanpos = xsp;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_108() {
    if (jj_3R_96()) return true;
    return false;
  }

  final private boolean jj_3R_221() {
    if (jj_3R_229()) return true;
    return false;
  }

  final private boolean jj_3R_107() {
    if (jj_3R_128()) return true;
    return false;
  }

  final private boolean jj_3R_106() {
    if (jj_3R_83()) return true;
    return false;
  }

  final private boolean jj_3R_82() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_106()) {
    jj_scanpos = xsp;
    if (jj_3R_107()) {
    jj_scanpos = xsp;
    if (jj_3R_108()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_143() {
    if (jj_scan_token(BIT_AND)) return true;
    if (jj_3R_123()) return true;
    return false;
  }

  final private boolean jj_3R_220() {
    if (jj_3R_228()) return true;
    return false;
  }

  final private boolean jj_3R_219() {
    if (jj_3R_227()) return true;
    return false;
  }

  final private boolean jj_3R_287() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_3R_82()) return true;
    return false;
  }

  final private boolean jj_3R_218() {
    if (jj_3R_226()) return true;
    return false;
  }

  final private boolean jj_3R_217() {
    if (jj_3R_146()) return true;
    return false;
  }

  final private boolean jj_3R_277() {
    if (jj_3R_287()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_288()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_276() {
    if (jj_3R_277()) return true;
    return false;
  }

  final private boolean jj_3R_81() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(ASSIGN)) return true;
    return false;
  }

  final private boolean jj_3R_213() {
    if (jj_3R_216()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_217()) {
    jj_scanpos = xsp;
    if (jj_3R_218()) {
    jj_scanpos = xsp;
    if (jj_3R_219()) {
    jj_scanpos = xsp;
    if (jj_3R_220()) {
    jj_scanpos = xsp;
    if (jj_3R_221()) return true;
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_130() {
    if (jj_scan_token(AT)) return true;
    if (jj_3R_80()) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_82()) return true;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3_4() {
    if (jj_3R_62()) return true;
    return false;
  }

  final private boolean jj_3R_210() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3R_213()) {
    jj_scanpos = xsp;
    if (jj_scan_token(83)) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_267() {
    if (jj_3R_201()) return true;
    return false;
  }

  final private boolean jj_3R_207() {
    if (jj_3R_210()) return true;
    return false;
  }

  final private boolean jj_3R_131() {
    if (jj_scan_token(AT)) return true;
    if (jj_3R_80()) return true;
    return false;
  }

  final private boolean jj_3R_113() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_112()) return true;
    return false;
  }

  final private boolean jj_3R_132() {
    if (jj_3R_137()) return true;
    return false;
  }

  final private boolean jj_3_38() {
    if (jj_scan_token(AT)) return true;
    if (jj_3R_80()) return true;
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_129() {
    if (jj_scan_token(AT)) return true;
    if (jj_3R_80()) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_276()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_201() {
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_207()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3_37() {
    if (jj_scan_token(AT)) return true;
    if (jj_3R_80()) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_81()) {
    jj_scanpos = xsp;
    if (jj_scan_token(78)) return true;
    }
    return false;
  }

  final private boolean jj_3R_137() {
    if (jj_scan_token(EXTENDS)) return true;
    if (jj_3R_123()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_143()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_111() {
    if (jj_3R_131()) return true;
    return false;
  }

  final private boolean jj_3R_110() {
    if (jj_3R_130()) return true;
    return false;
  }

  final private boolean jj_3R_266() {
    if (jj_3R_66()) return true;
    return false;
  }

  final private boolean jj_3R_112() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_132()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_109() {
    if (jj_3R_129()) return true;
    return false;
  }

  final private boolean jj_3R_97() {
    return false;
  }

  final private boolean jj_3R_83() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_109()) {
    jj_scanpos = xsp;
    if (jj_3R_110()) {
    jj_scanpos = xsp;
    if (jj_3R_111()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_255() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_254()) return true;
    return false;
  }

  final private boolean jj_3R_84() {
    if (jj_scan_token(LT)) return true;
    if (jj_3R_112()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_113()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(GT)) return true;
    return false;
  }

  final private boolean jj_3R_98() {
    return false;
  }

  final private boolean jj_3R_268() {
    if (jj_3R_210()) return true;
    return false;
  }

  final private boolean jj_3R_71() {
    lookingAhead = true;
    jj_semLA = getToken(1).kind == GT &&
                ((Token.GTToken)getToken(1)).realKind == RSIGNEDSHIFT;
    lookingAhead = false;
    if (!jj_semLA || jj_3R_97()) return true;
    if (jj_scan_token(GT)) return true;
    if (jj_scan_token(GT)) return true;
    return false;
  }

  final private boolean jj_3R_254() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_266()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_267()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_256() {
    if (jj_scan_token(SEMICOLON)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_268()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_236() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_3R_254()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_255()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_3R_256()) jj_scanpos = xsp;
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_72() {
    lookingAhead = true;
    jj_semLA = getToken(1).kind == GT &&
                ((Token.GTToken)getToken(1)).realKind == RUNSIGNEDSHIFT;
    lookingAhead = false;
    if (!jj_semLA || jj_3R_98()) return true;
    if (jj_scan_token(GT)) return true;
    if (jj_scan_token(GT)) return true;
    if (jj_scan_token(GT)) return true;
    return false;
  }

  final private boolean jj_3R_235() {
    if (jj_3R_253()) return true;
    return false;
  }

  final private boolean jj_3R_286() {
    if (jj_scan_token(FINALLY)) return true;
    if (jj_3R_86()) return true;
    return false;
  }

  final private boolean jj_3R_285() {
    if (jj_scan_token(CATCH)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_269()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_86()) return true;
    return false;
  }

  final private boolean jj_3R_226() {
    if (jj_scan_token(ENUM)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_235()) jj_scanpos = xsp;
    if (jj_3R_236()) return true;
    return false;
  }

  final private boolean jj_3R_180() {
    if (jj_scan_token(TRY)) return true;
    if (jj_3R_86()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_285()) { jj_scanpos = xsp; break; }
    }
    xsp = jj_scanpos;
    if (jj_3R_286()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_265() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_123()) return true;
    return false;
  }

  final private boolean jj_3R_253() {
    if (jj_scan_token(IMPLEMENTS)) return true;
    if (jj_3R_123()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_265()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_179() {
    if (jj_scan_token(SYNCHRONIZED)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_86()) return true;
    return false;
  }

  final private boolean jj_3R_284() {
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_264() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_123()) return true;
    return false;
  }

  final private boolean jj_3R_178() {
    if (jj_scan_token(THROW)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_252() {
    if (jj_scan_token(EXTENDS)) return true;
    if (jj_3R_123()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_264()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_294() {
    if (jj_3R_299()) return true;
    return false;
  }

  final private boolean jj_3R_164() {
    if (jj_scan_token(INTERFACE)) return true;
    return false;
  }

  final private boolean jj_3R_303() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_169()) return true;
    return false;
  }

  final private boolean jj_3R_177() {
    if (jj_scan_token(RETURN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_284()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_234() {
    if (jj_3R_253()) return true;
    return false;
  }

  final private boolean jj_3R_233() {
    if (jj_3R_252()) return true;
    return false;
  }

  final private boolean jj_3R_232() {
    if (jj_3R_84()) return true;
    return false;
  }

  final private boolean jj_3R_176() {
    if (jj_scan_token(CONTINUE)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(74)) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_146() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(21)) {
    jj_scanpos = xsp;
    if (jj_3R_164()) return true;
    }
    if (jj_scan_token(IDENTIFIER)) return true;
    xsp = jj_scanpos;
    if (jj_3R_232()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_233()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_234()) jj_scanpos = xsp;
    if (jj_3R_201()) return true;
    return false;
  }

  final private boolean jj_3R_175() {
    if (jj_scan_token(BREAK)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(74)) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_299() {
    if (jj_3R_302()) return true;
    return false;
  }

  final private boolean jj_3R_293() {
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3_36() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(31)) jj_scanpos = xsp;
    if (jj_3R_60()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_302() {
    if (jj_3R_169()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_303()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_281() {
    if (jj_scan_token(ELSE)) return true;
    if (jj_3R_145()) return true;
    return false;
  }

  final private boolean jj_3R_301() {
    if (jj_3R_302()) return true;
    return false;
  }

  final private boolean jj_3_35() {
    if (jj_3R_60()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  final private boolean jj_3R_300() {
    if (jj_3R_144()) return true;
    return false;
  }

  final private boolean jj_3R_298() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_300()) {
    jj_scanpos = xsp;
    if (jj_3R_301()) return true;
    }
    return false;
  }

  final private boolean jj_3R_292() {
    if (jj_3R_298()) return true;
    return false;
  }

  final private boolean jj_3R_283() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_292()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    xsp = jj_scanpos;
    if (jj_3R_293()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    xsp = jj_scanpos;
    if (jj_3R_294()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_282() {
    if (jj_3R_60()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_58() {
    if (jj_3R_83()) return true;
    return false;
  }

  final private boolean jj_3R_57() {
    if (jj_scan_token(STRICTFP)) return true;
    return false;
  }

  final private boolean jj_3R_56() {
    if (jj_scan_token(VOLATILE)) return true;
    return false;
  }

  final private boolean jj_3R_174() {
    if (jj_scan_token(FOR)) return true;
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_282()) {
    jj_scanpos = xsp;
    if (jj_3R_283()) return true;
    }
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_145()) return true;
    return false;
  }

  final private boolean jj_3R_55() {
    if (jj_scan_token(TRANSIENT)) return true;
    return false;
  }

  final private boolean jj_3R_54() {
    if (jj_scan_token(NATIVE)) return true;
    return false;
  }

  final private boolean jj_3R_53() {
    if (jj_scan_token(SYNCHRONIZED)) return true;
    return false;
  }

  final private boolean jj_3R_173() {
    if (jj_scan_token(DO)) return true;
    if (jj_3R_145()) return true;
    if (jj_scan_token(WHILE)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_52() {
    if (jj_scan_token(ABSTRACT)) return true;
    return false;
  }

  final private boolean jj_3R_51() {
    if (jj_scan_token(FINAL)) return true;
    return false;
  }

  final private boolean jj_3R_50() {
    if (jj_scan_token(PRIVATE)) return true;
    return false;
  }

  final private boolean jj_3R_172() {
    if (jj_scan_token(WHILE)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_145()) return true;
    return false;
  }

  final private boolean jj_3R_49() {
    if (jj_scan_token(PROTECTED)) return true;
    return false;
  }

  final private boolean jj_3R_48() {
    if (jj_scan_token(STATIC)) return true;
    return false;
  }

  final private boolean jj_3R_291() {
    if (jj_3R_133()) return true;
    return false;
  }

  final private boolean jj_3R_47() {
    if (jj_scan_token(PUBLIC)) return true;
    return false;
  }

  final private boolean jj_3R_171() {
    if (jj_scan_token(IF)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_145()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_281()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3_1() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_47()) {
    jj_scanpos = xsp;
    if (jj_3R_48()) {
    jj_scanpos = xsp;
    if (jj_3R_49()) {
    jj_scanpos = xsp;
    if (jj_3R_50()) {
    jj_scanpos = xsp;
    if (jj_3R_51()) {
    jj_scanpos = xsp;
    if (jj_3R_52()) {
    jj_scanpos = xsp;
    if (jj_3R_53()) {
    jj_scanpos = xsp;
    if (jj_3R_54()) {
    jj_scanpos = xsp;
    if (jj_3R_55()) {
    jj_scanpos = xsp;
    if (jj_3R_56()) {
    jj_scanpos = xsp;
    if (jj_3R_57()) {
    jj_scanpos = xsp;
    if (jj_3R_58()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_216() {
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_1()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_297() {
    if (jj_scan_token(_DEFAULT)) return true;
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  final private boolean jj_3R_296() {
    if (jj_scan_token(CASE)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(COLON)) return true;
    return false;
  }

  final private boolean jj_3R_290() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_296()) {
    jj_scanpos = xsp;
    if (jj_3R_297()) return true;
    }
    return false;
  }

  final private boolean jj_3R_275() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_242()) return true;
    return false;
  }

  final private boolean jj_3R_280() {
    if (jj_3R_290()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_291()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_170() {
    if (jj_scan_token(SWITCH)) return true;
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_280()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_295() {
    if (jj_3R_69()) return true;
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_289() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(99)) {
    jj_scanpos = xsp;
    if (jj_scan_token(100)) {
    jj_scanpos = xsp;
    if (jj_3R_295()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_185() {
    if (jj_3R_65()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_289()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_184() {
    if (jj_3R_194()) return true;
    return false;
  }

  final private boolean jj_3R_169() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_183()) {
    jj_scanpos = xsp;
    if (jj_3R_184()) {
    jj_scanpos = xsp;
    if (jj_3R_185()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_183() {
    if (jj_3R_193()) return true;
    return false;
  }

  final private boolean jj_3R_144() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(31)) jj_scanpos = xsp;
    if (jj_3R_60()) return true;
    if (jj_3R_242()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_275()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3_34() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(31)) jj_scanpos = xsp;
    if (jj_3R_60()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_279() {
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_140() {
    if (jj_3R_146()) return true;
    return false;
  }

  final private boolean jj_3R_139() {
    if (jj_3R_145()) return true;
    return false;
  }

  final private boolean jj_3R_138() {
    if (jj_3R_144()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_133() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_138()) {
    jj_scanpos = xsp;
    if (jj_3R_139()) {
    jj_scanpos = xsp;
    if (jj_3R_140()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_114() {
    if (jj_3R_133()) return true;
    return false;
  }

  final private boolean jj_3R_86() {
    if (jj_scan_token(LBRACE)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_114()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(RBRACE)) return true;
    return false;
  }

  final private boolean jj_3R_79() {
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_145()) return true;
    return false;
  }

  final private boolean jj_3R_168() {
    if (jj_scan_token(ASSERT)) return true;
    if (jj_3R_70()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_279()) jj_scanpos = xsp;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_163() {
    if (jj_3R_180()) return true;
    return false;
  }

  final private boolean jj_3R_162() {
    if (jj_3R_179()) return true;
    return false;
  }

  final private boolean jj_3_31() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_161() {
    if (jj_3R_178()) return true;
    return false;
  }

  final private boolean jj_3R_160() {
    if (jj_3R_177()) return true;
    return false;
  }

  final private boolean jj_3R_159() {
    if (jj_3R_176()) return true;
    return false;
  }

  final private boolean jj_3R_158() {
    if (jj_3R_175()) return true;
    return false;
  }

  final private boolean jj_3R_157() {
    if (jj_3R_174()) return true;
    return false;
  }

  final private boolean jj_3R_156() {
    if (jj_3R_173()) return true;
    return false;
  }

  final private boolean jj_3R_155() {
    if (jj_3R_172()) return true;
    return false;
  }

  final private boolean jj_3R_154() {
    if (jj_3R_171()) return true;
    return false;
  }

  final private boolean jj_3R_153() {
    if (jj_3R_170()) return true;
    return false;
  }

  final private boolean jj_3R_152() {
    if (jj_3R_169()) return true;
    if (jj_scan_token(SEMICOLON)) return true;
    return false;
  }

  final private boolean jj_3R_151() {
    if (jj_3R_86()) return true;
    return false;
  }

  final private boolean jj_3R_150() {
    if (jj_3R_168()) return true;
    return false;
  }

  final private boolean jj_3R_187() {
    if (jj_3R_68()) return true;
    return false;
  }

  final private boolean jj_3_33() {
    if (jj_3R_79()) return true;
    return false;
  }

  final private boolean jj_3R_145() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_33()) {
    jj_scanpos = xsp;
    if (jj_3R_150()) {
    jj_scanpos = xsp;
    if (jj_3R_151()) {
    jj_scanpos = xsp;
    if (jj_scan_token(83)) {
    jj_scanpos = xsp;
    if (jj_3R_152()) {
    jj_scanpos = xsp;
    if (jj_3R_153()) {
    jj_scanpos = xsp;
    if (jj_3R_154()) {
    jj_scanpos = xsp;
    if (jj_3R_155()) {
    jj_scanpos = xsp;
    if (jj_3R_156()) {
    jj_scanpos = xsp;
    if (jj_3R_157()) {
    jj_scanpos = xsp;
    if (jj_3R_158()) {
    jj_scanpos = xsp;
    if (jj_3R_159()) {
    jj_scanpos = xsp;
    if (jj_3R_160()) {
    jj_scanpos = xsp;
    if (jj_3R_161()) {
    jj_scanpos = xsp;
    if (jj_3R_162()) {
    jj_scanpos = xsp;
    if (jj_3R_163()) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_196() {
    if (jj_3R_201()) return true;
    return false;
  }

  final private boolean jj_3R_200() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3_30() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_195() {
    Token xsp;
    if (jj_3R_200()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_200()) { jj_scanpos = xsp; break; }
    }
    if (jj_3R_115()) return true;
    return false;
  }

  final private boolean jj_3_32() {
    Token xsp;
    if (jj_3_30()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_30()) { jj_scanpos = xsp; break; }
    }
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_31()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_186() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_32()) {
    jj_scanpos = xsp;
    if (jj_3R_195()) return true;
    }
    return false;
  }

  final private boolean jj_3R_189() {
    if (jj_3R_66()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_196()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_135() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_188() {
    if (jj_3R_186()) return true;
    return false;
  }

  final private boolean jj_3R_105() {
    if (jj_scan_token(NEW)) return true;
    if (jj_3R_123()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_187()) jj_scanpos = xsp;
    xsp = jj_scanpos;
    if (jj_3R_188()) {
    jj_scanpos = xsp;
    if (jj_3R_189()) return true;
    }
    return false;
  }

  final private boolean jj_3_29() {
    if (jj_scan_token(NEW)) return true;
    if (jj_3R_74()) return true;
    if (jj_3R_186()) return true;
    return false;
  }

  final private boolean jj_3R_77() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_29()) {
    jj_scanpos = xsp;
    if (jj_3R_105()) return true;
    }
    return false;
  }

  final private boolean jj_3R_122() {
    if (jj_3R_70()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_135()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_92() {
    if (jj_3R_122()) return true;
    return false;
  }

  final private boolean jj_3R_66() {
    if (jj_scan_token(LPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_92()) jj_scanpos = xsp;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3R_147() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(61)) {
    jj_scanpos = xsp;
    if (jj_scan_token(30)) return true;
    }
    return false;
  }

  final private boolean jj_3R_141() {
    if (jj_3R_147()) return true;
    return false;
  }

  final private boolean jj_3R_127() {
    if (jj_3R_134()) return true;
    return false;
  }

  final private boolean jj_3R_134() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(66)) {
    jj_scanpos = xsp;
    if (jj_scan_token(70)) {
    jj_scanpos = xsp;
    if (jj_scan_token(72)) {
    jj_scanpos = xsp;
    if (jj_scan_token(73)) {
    jj_scanpos = xsp;
    if (jj_3R_141()) {
    jj_scanpos = xsp;
    if (jj_scan_token(45)) return true;
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_103() {
    if (jj_3R_66()) return true;
    return false;
  }

  final private boolean jj_3R_102() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_101() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3_28() {
    if (jj_3R_78()) return true;
    return false;
  }

  final private boolean jj_3_27() {
    if (jj_scan_token(DOT)) return true;
    if (jj_3R_77()) return true;
    return false;
  }

  final private boolean jj_3_25() {
    if (jj_3R_76()) return true;
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(CLASS)) return true;
    return false;
  }

  final private boolean jj_3_26() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(THIS)) return true;
    return false;
  }

  final private boolean jj_3R_75() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_26()) {
    jj_scanpos = xsp;
    if (jj_3_27()) {
    jj_scanpos = xsp;
    if (jj_3_28()) {
    jj_scanpos = xsp;
    if (jj_3R_101()) {
    jj_scanpos = xsp;
    if (jj_3R_102()) {
    jj_scanpos = xsp;
    if (jj_3R_103()) return true;
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_121() {
    if (jj_3R_80()) return true;
    return false;
  }

  final private boolean jj_3R_120() {
    if (jj_3R_76()) return true;
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(CLASS)) return true;
    return false;
  }

  final private boolean jj_3R_119() {
    if (jj_3R_77()) return true;
    return false;
  }

  final private boolean jj_3R_118() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(RPAREN)) return true;
    return false;
  }

  final private boolean jj_3_24() {
    if (jj_3R_75()) return true;
    return false;
  }

  final private boolean jj_3R_117() {
    if (jj_scan_token(SUPER)) return true;
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_116() {
    if (jj_3R_134()) return true;
    return false;
  }

  final private boolean jj_3R_91() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_116()) {
    jj_scanpos = xsp;
    if (jj_scan_token(57)) {
    jj_scanpos = xsp;
    if (jj_3R_117()) {
    jj_scanpos = xsp;
    if (jj_3R_118()) {
    jj_scanpos = xsp;
    if (jj_3R_119()) {
    jj_scanpos = xsp;
    if (jj_3R_120()) {
    jj_scanpos = xsp;
    if (jj_3R_121()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_278() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(99)) {
    jj_scanpos = xsp;
    if (jj_scan_token(100)) return true;
    }
    return false;
  }

  final private boolean jj_3R_78() {
    if (jj_scan_token(DOT)) return true;
    if (jj_3R_68()) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3_23() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_74()) return true;
    return false;
  }

  final private boolean jj_3R_65() {
    if (jj_3R_91()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_24()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_274() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_60()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_231()) return true;
    return false;
  }

  final private boolean jj_3R_273() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_60()) return true;
    if (jj_scan_token(RPAREN)) return true;
    if (jj_3R_215()) return true;
    return false;
  }

  final private boolean jj_3R_262() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_273()) {
    jj_scanpos = xsp;
    if (jj_3R_274()) return true;
    }
    return false;
  }

  final private boolean jj_3_22() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_60()) return true;
    if (jj_scan_token(LBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_263() {
    if (jj_3R_65()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_278()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_100() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_60()) return true;
    if (jj_scan_token(RPAREN)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(90)) {
    jj_scanpos = xsp;
    if (jj_scan_token(89)) {
    jj_scanpos = xsp;
    if (jj_scan_token(77)) {
    jj_scanpos = xsp;
    if (jj_scan_token(74)) {
    jj_scanpos = xsp;
    if (jj_scan_token(57)) {
    jj_scanpos = xsp;
    if (jj_scan_token(54)) {
    jj_scanpos = xsp;
    if (jj_scan_token(44)) {
    jj_scanpos = xsp;
    if (jj_3R_127()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_99() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_60()) return true;
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3_21() {
    if (jj_scan_token(LPAREN)) return true;
    if (jj_3R_74()) return true;
    return false;
  }

  final private boolean jj_3R_73() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_21()) {
    jj_scanpos = xsp;
    if (jj_3R_99()) {
    jj_scanpos = xsp;
    if (jj_3R_100()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3_20() {
    if (jj_3R_73()) return true;
    return false;
  }

  final private boolean jj_3_19() {
    if (jj_3R_72()) return true;
    return false;
  }

  final private boolean jj_3R_251() {
    if (jj_3R_263()) return true;
    return false;
  }

  final private boolean jj_3R_250() {
    if (jj_3R_262()) return true;
    return false;
  }

  final private boolean jj_3R_231() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_249()) {
    jj_scanpos = xsp;
    if (jj_3R_250()) {
    jj_scanpos = xsp;
    if (jj_3R_251()) return true;
    }
    }
    return false;
  }

  final private boolean jj_3R_249() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(90)) {
    jj_scanpos = xsp;
    if (jj_scan_token(89)) return true;
    }
    if (jj_3R_215()) return true;
    return false;
  }

  final private boolean jj_3R_194() {
    if (jj_scan_token(DECR)) return true;
    if (jj_3R_65()) return true;
    return false;
  }

  final private boolean jj_3R_230() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(101)) {
    jj_scanpos = xsp;
    if (jj_scan_token(102)) return true;
    }
    if (jj_3R_212()) return true;
    return false;
  }

  final private boolean jj_3_18() {
    if (jj_3R_71()) return true;
    return false;
  }

  final private boolean jj_3R_248() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(103)) {
    jj_scanpos = xsp;
    if (jj_scan_token(104)) {
    jj_scanpos = xsp;
    if (jj_scan_token(108)) return true;
    }
    }
    if (jj_3R_215()) return true;
    return false;
  }

  final private boolean jj_3R_193() {
    if (jj_scan_token(INCR)) return true;
    if (jj_3R_65()) return true;
    return false;
  }

  final private boolean jj_3R_225() {
    if (jj_3R_231()) return true;
    return false;
  }

  final private boolean jj_3_17() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(109)) {
    jj_scanpos = xsp;
    if (jj_3_18()) {
    jj_scanpos = xsp;
    if (jj_3_19()) return true;
    }
    }
    if (jj_3R_209()) return true;
    return false;
  }

  final private boolean jj_3R_224() {
    if (jj_3R_194()) return true;
    return false;
  }

  final private boolean jj_3R_223() {
    if (jj_3R_193()) return true;
    return false;
  }

  final private boolean jj_3R_215() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_222()) {
    jj_scanpos = xsp;
    if (jj_3R_223()) {
    jj_scanpos = xsp;
    if (jj_3R_224()) {
    jj_scanpos = xsp;
    if (jj_3R_225()) return true;
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_222() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(101)) {
    jj_scanpos = xsp;
    if (jj_scan_token(102)) return true;
    }
    if (jj_3R_215()) return true;
    return false;
  }

  final private boolean jj_3R_214() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(88)) {
    jj_scanpos = xsp;
    if (jj_scan_token(124)) {
    jj_scanpos = xsp;
    if (jj_scan_token(94)) {
    jj_scanpos = xsp;
    if (jj_scan_token(95)) return true;
    }
    }
    }
    if (jj_3R_203()) return true;
    return false;
  }

  final private boolean jj_3R_211() {
    if (jj_scan_token(INSTANCEOF)) return true;
    if (jj_3R_60()) return true;
    return false;
  }

  final private boolean jj_3R_212() {
    if (jj_3R_215()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_248()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_208() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(93)) {
    jj_scanpos = xsp;
    if (jj_scan_token(96)) return true;
    }
    if (jj_3R_191()) return true;
    return false;
  }

  final private boolean jj_3R_209() {
    if (jj_3R_212()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_230()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_203() {
    if (jj_3R_209()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_17()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_202() {
    if (jj_scan_token(BIT_AND)) return true;
    if (jj_3R_182()) return true;
    return false;
  }

  final private boolean jj_3R_198() {
    if (jj_3R_203()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_214()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_190() {
    if (jj_scan_token(BIT_OR)) return true;
    if (jj_3R_149()) return true;
    return false;
  }

  final private boolean jj_3R_191() {
    if (jj_3R_198()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_211()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_197() {
    if (jj_scan_token(XOR)) return true;
    if (jj_3R_166()) return true;
    return false;
  }

  final private boolean jj_3R_181() {
    if (jj_scan_token(SC_AND)) return true;
    if (jj_3R_142()) return true;
    return false;
  }

  final private boolean jj_3R_182() {
    if (jj_3R_191()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_208()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_165() {
    if (jj_scan_token(SC_OR)) return true;
    if (jj_3R_136()) return true;
    return false;
  }

  final private boolean jj_3R_166() {
    if (jj_3R_182()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_202()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_148() {
    if (jj_scan_token(HOOK)) return true;
    if (jj_3R_70()) return true;
    if (jj_scan_token(COLON)) return true;
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_149() {
    if (jj_3R_166()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_197()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_142() {
    if (jj_3R_149()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_190()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_136() {
    if (jj_3R_142()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_181()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_126() {
    if (jj_3R_136()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_165()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_96() {
    if (jj_3R_126()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_148()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_69() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(87)) {
    jj_scanpos = xsp;
    if (jj_scan_token(112)) {
    jj_scanpos = xsp;
    if (jj_scan_token(113)) {
    jj_scanpos = xsp;
    if (jj_scan_token(117)) {
    jj_scanpos = xsp;
    if (jj_scan_token(110)) {
    jj_scanpos = xsp;
    if (jj_scan_token(111)) {
    jj_scanpos = xsp;
    if (jj_scan_token(118)) {
    jj_scanpos = xsp;
    if (jj_scan_token(119)) {
    jj_scanpos = xsp;
    if (jj_scan_token(120)) {
    jj_scanpos = xsp;
    if (jj_scan_token(114)) {
    jj_scanpos = xsp;
    if (jj_scan_token(116)) {
    jj_scanpos = xsp;
    if (jj_scan_token(115)) return true;
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3_16() {
    if (jj_3R_69()) return true;
    if (jj_3R_70()) return true;
    return false;
  }

  final private boolean jj_3R_70() {
    if (jj_3R_96()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_16()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_271() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_80()) return true;
    return false;
  }

  final private boolean jj_3R_258() {
    if (jj_3R_80()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_271()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3_15() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  final private boolean jj_3R_80() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_15()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  final private boolean jj_3R_104() {
    if (jj_3R_60()) return true;
    return false;
  }

  final private boolean jj_3R_76() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(63)) {
    jj_scanpos = xsp;
    if (jj_3R_104()) return true;
    }
    return false;
  }

  final private boolean jj_3_14() {
    if (jj_3R_68()) return true;
    return false;
  }

  final private boolean jj_3R_74() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(15)) {
    jj_scanpos = xsp;
    if (jj_scan_token(20)) {
    jj_scanpos = xsp;
    if (jj_scan_token(17)) {
    jj_scanpos = xsp;
    if (jj_scan_token(51)) {
    jj_scanpos = xsp;
    if (jj_scan_token(40)) {
    jj_scanpos = xsp;
    if (jj_scan_token(42)) {
    jj_scanpos = xsp;
    if (jj_scan_token(33)) {
    jj_scanpos = xsp;
    if (jj_scan_token(26)) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  final private boolean jj_3R_167() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_95()) return true;
    return false;
  }

  final private boolean jj_3_11() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_205() {
    if (jj_scan_token(SUPER)) return true;
    if (jj_3R_67()) return true;
    return false;
  }

  final private boolean jj_3R_192() {
    if (jj_3R_199()) return true;
    return false;
  }

  final private boolean jj_3R_199() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_204()) {
    jj_scanpos = xsp;
    if (jj_3R_205()) return true;
    }
    return false;
  }

  final private boolean jj_3R_204() {
    if (jj_scan_token(EXTENDS)) return true;
    if (jj_3R_67()) return true;
    return false;
  }

  final private boolean jj_3R_125() {
    if (jj_scan_token(HOOK)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_192()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3_12() {
    if (jj_3R_68()) return true;
    return false;
  }

  final private boolean jj_3R_124() {
    if (jj_3R_67()) return true;
    return false;
  }

  final private boolean jj_3R_95() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_124()) {
    jj_scanpos = xsp;
    if (jj_3R_125()) return true;
    }
    return false;
  }

  final private boolean jj_3_10() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_scan_token(RBRACKET)) return true;
    return false;
  }

  final private boolean jj_3R_68() {
    if (jj_scan_token(LT)) return true;
    if (jj_3R_95()) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_167()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(GT)) return true;
    return false;
  }

  final private boolean jj_3_13() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_14()) jj_scanpos = xsp;
    return false;
  }

  final private boolean jj_3R_123() {
    if (jj_scan_token(IDENTIFIER)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_12()) jj_scanpos = xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_13()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  public JavaParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;

  public JavaParser(java.io.InputStream stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new JavaParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public JavaParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new JavaParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
  }

  public JavaParser(JavaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
  }

  public void ReInit(JavaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      return token;
    }
    token = oldToken;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  public ParseException generateParseException() {
    Token errortok = token.next;
    int line = errortok.beginLine, column = errortok.beginColumn;
    String mess = (errortok.kind == 0) ? tokenImage[0] : errortok.image;
    return new ParseException(FlowGenerator.mlp.getString("error_line")+" "+ line + ", "+FlowGenerator.mlp.getString("error_column")+" "+ column + ".  Token: " + mess);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}