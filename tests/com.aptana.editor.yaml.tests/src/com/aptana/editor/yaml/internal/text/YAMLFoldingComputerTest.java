/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.yaml.internal.text;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Position;

import com.aptana.editor.common.text.reconciler.IFoldingComputer;
import com.aptana.editor.yaml.parsing.YAMLParser;
import com.aptana.parsing.IParseState;
import com.aptana.parsing.ParseState;
import com.aptana.parsing.ast.IParseNode;

public class YAMLFoldingComputerTest extends TestCase
{

	private IFoldingComputer folder;

	@Override
	protected void tearDown() throws Exception
	{
		folder = null;
		super.tearDown();
	}

	public void testBasicYAMLFolding() throws Exception
	{
		String src = "development:\n  adapter: mysql\n\ntest:\n  adapter: sqlite3\n";
		folder = new YAMLFoldingComputer(null, new Document(src))
		{
			protected IParseNode getAST()
			{
				IParseState parseState = new ParseState();
				parseState.setEditState(getDocument().get(), null, 0, 0);
				try
				{
					return new YAMLParser().parse(parseState);
				}
				catch (Exception e)
				{
					fail(e.getMessage());
				}
				return null;
			};
		};
		List<Position> positions = folder.emitFoldingRegions(new NullProgressMonitor());
		assertEquals(2, positions.size());
		assertEquals(new Position(0, 30), positions.get(0));
		assertEquals(new Position(31, 25), positions.get(1));
	}
}