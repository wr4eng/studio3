/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.html.parsing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

import com.aptana.editor.common.text.rules.PartitionerSwitchingIgnoreRule;
import com.aptana.editor.common.text.rules.TagRule;
import com.aptana.editor.common.text.rules.WhitespaceDetector;
import com.aptana.editor.common.text.rules.WordDetector;
import com.aptana.editor.html.parsing.lexer.HTMLTokens;

public class HTMLTokenScanner extends RuleBasedScanner
{

	public HTMLTokenScanner()
	{
		List<IRule> rules = new ArrayList<IRule>();
		// generic whitespace rule
		rules.add(new WhitespaceRule(new WhitespaceDetector()));
		// comments
		rules.add(new PartitionerSwitchingIgnoreRule(new MultiLineRule(
				"<!--", "-->", createToken(getTokenName(HTMLTokens.COMMENT)), (char) 0, true))); //$NON-NLS-1$ //$NON-NLS-2$
		// DOCTYPE
		rules.add(new MultiLineRule("<!DOCTYPE ", ">", createToken(getTokenName(HTMLTokens.DOCTYPE)))); //$NON-NLS-1$ //$NON-NLS-2$
		// CDATA
		rules.add(new MultiLineRule("<![CDATA[", "]]>", createToken(getTokenName(HTMLTokens.CDATA)))); //$NON-NLS-1$ //$NON-NLS-2$
		// script
		rules.add(new TagRule("script", createToken(getTokenName(HTMLTokens.SCRIPT)), true)); //$NON-NLS-1$
		rules.add(new TagRule("/script", createToken(getTokenName(HTMLTokens.SCRIPT_END)), true)); //$NON-NLS-1$
		// style
		rules.add(new TagRule("style", createToken(getTokenName(HTMLTokens.STYLE)), true)); //$NON-NLS-1$
		rules.add(new TagRule("/style", createToken(getTokenName(HTMLTokens.STYLE_END)), true)); //$NON-NLS-1$
		// xml declaration
		rules.add(new TagRule("?xml", createToken(getTokenName(HTMLTokens.XML_DECL)))); //$NON-NLS-1$
		// tags
		rules.add(new TagRule("/", createToken(getTokenName(HTMLTokens.END_TAG)))); //$NON-NLS-1$
		rules.add(new TagRule(createToken(getTokenName(HTMLTokens.START_TAG))));

		// text
		IToken token = createToken(getTokenName(HTMLTokens.TEXT));
		rules.add(new WordRule(new WordDetector(), token));

		setRules(rules.toArray(new IRule[rules.size()]));
		setDefaultReturnToken(token);
	}

	protected IToken createToken(String string)
	{
		return new Token(string);
	}

	private static String getTokenName(short token)
	{
		return HTMLTokens.getTokenName(token);
	}
}
