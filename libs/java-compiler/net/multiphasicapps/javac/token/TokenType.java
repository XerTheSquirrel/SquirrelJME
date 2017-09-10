// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
 * This represents the type of token which was parsed.
 *
 * @since 2017/09/04
 */
public enum TokenType
{
	/** A comment. */
	COMMENT,
	
	/** Identifier. */
	IDENTIFIER,
	
	/** Keyword abstract. */
	KEYWORD_ABSTRACT,
	
	/** Keyword assert. */
	KEYWORD_ASSERT,
	
	/** Keyword boolean. */
	KEYWORD_BOOLEAN,
	
	/** Keyword break. */
	KEYWORD_BREAK,
	
	/** Keyword byte. */
	KEYWORD_BYTE,
	
	/** Keyword case. */
	KEYWORD_CASE,
	
	/** Keyword catch. */
	KEYWORD_CATCH,
	
	/** Keyword char. */
	KEYWORD_CHAR,
	
	/** Keyword class. */
	KEYWORD_CLASS,
	
	/** Keyword continue. */
	KEYWORD_CONTINUE,
	
	/** Keyword default. */
	KEYWORD_DEFAULT,
	
	/** Keyword do. */
	KEYWORD_DO,
	
	/** Keyword double. */
	KEYWORD_DOUBLE,
	
	/** Keyword else. */
	KEYWORD_ELSE,
	
	/** Keyword enum. */
	KEYWORD_ENUM,
	
	/** Keyword extends. */
	KEYWORD_EXTENDS,
	
	/** Keyword final. */
	KEYWORD_FINAL,
	
	/** Keyword finally. */
	KEYWORD_FINALLY,
	
	/** Keyword float. */
	KEYWORD_FLOAT,
	
	/** Keyword for. */
	KEYWORD_FOR,
	
	/** Keyword if. */
	KEYWORD_IF,
	
	/** Keyword implements. */
	KEYWORD_IMPLEMENTS,
	
	/** Keyword import. */
	KEYWORD_IMPORT,
	
	/** Keyword instanceof. */
	KEYWORD_INSTANCEOF,
	
	/** Keyword int. */
	KEYWORD_INT,
	
	/** Keyword interface. */
	KEYWORD_INTERFACE,
	
	/** Keyword long. */
	KEYWORD_LONG,
	
	/** Keyword native. */
	KEYWORD_NATIVE,
	
	/** Keyword new. */
	KEYWORD_NEW,
	
	/** Keyword package. */
	KEYWORD_PACKAGE,
	
	/** Keyword private. */
	KEYWORD_PRIVATE,
	
	/** Keyword protected. */
	KEYWORD_PROTECTED,
	
	/** Keyword public. */
	KEYWORD_PUBLIC,
	
	/** Keyword return. */
	KEYWORD_RETURN,
	
	/** Keyword short. */
	KEYWORD_SHORT,
	
	/** Keyword static. */
	KEYWORD_STATIC,
	
	/** Keyword strictfp. */
	KEYWORD_STRICTFP,
	
	/** Keyword super. */
	KEYWORD_SUPER,
	
	/** Keyword switch. */
	KEYWORD_SWITCH,
	
	/** Keyword synchronized. */
	KEYWORD_SYNCHRONIZED,
	
	/** Keyword this. */
	KEYWORD_THIS,
	
	/** Keyword throw. */
	KEYWORD_THROW,
	
	/** Keyword throws. */
	KEYWORD_THROWS,
	
	/** Keyword transient. */
	KEYWORD_TRANSIENT,
	
	/** Keyword try. */
	KEYWORD_TRY,
	
	/** Keyword void. */
	KEYWORD_VOID,
	
	/** Keyword volatile. */
	KEYWORD_VOLATILE,
	
	/** Keyword while. */
	KEYWORD_WHILE,
	
	/** Divide operator. */
	OPERATOR_DIVIDE,
	
	/** Dive and assign operator. */
	OPERATOR_DIVIDE_AND_ASSIGN,
	
	/** End. */
	;
	
	/**
	 * Is this a comment?
	 *
	 * @return If this is a comment or not.
	 * @since 2017/09/06
	 */
	public final boolean isComment()
	{
		switch (this)
		{
				// Comments
			case COMMENT:
				return true;
			
				// Not a comment
			default:
				return false;
		}
	}
}

