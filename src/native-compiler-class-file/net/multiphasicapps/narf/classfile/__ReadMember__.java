// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIConstantValue;
import net.multiphasicapps.narf.classinterface.NCIException;
import net.multiphasicapps.narf.classinterface.NCIField;
import net.multiphasicapps.narf.classinterface.NCIFieldFlag;
import net.multiphasicapps.narf.classinterface.NCIFieldFlags;
import net.multiphasicapps.narf.classinterface.NCIFieldID;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIMethodFlag;
import net.multiphasicapps.narf.classinterface.NCIMethodFlags;
import net.multiphasicapps.narf.classinterface.NCIMethodID;
import net.multiphasicapps.narf.classinterface.NCIUTF;
import net.multiphasicapps.io.BufferAreaInputStream;

/**
 * This reads class members.
 *
 * @since 2016/04/26
 */
class __ReadMember__
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/04/26
	 */
	private __ReadMember__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Reads a field from the given class.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @param __into The target mappings.
	 * @throws IOException On read errors.
	 * @sicne 2016/04/26
	 */
	static void __field(NCFClass __oc, DataInputStream __das,
		Map<NCIFieldID, NCIField> __into)
		throws IOException
	{
		// Read flags
		int flags = __das.readUnsignedShort();
		
		// Read name and type
		IdentifierSymbol name = __readIdentifier(__oc, __das);
		FieldSymbol type = __readType(__oc, __das).asField();
		
		// Setup ID
		NCIFieldID id = new NCIFieldID(name, type);
		
		// Read in attributes
		Object constval = null;
		int na = __das.readUnsignedShort();
		for (int i = 0; i < na; i++)
		{
			// Read kind
			String kind = __oc.constantPool().<NCIUTF>requiredAs(
				__das.readUnsignedShort(), NCIUTF.class).toString();
			int len = __das.readInt();
			
			// {@squirreljme.error CF1q Field attribute has negative length.
			// (The field id)}
			if (len < 0)
				throw new NCIException(NCIException.Issue.NEGATIVE_ATTRIBUTE,
					String.format("CF1q", id));
			
			// Setup area
			try (BufferAreaInputStream bais = new BufferAreaInputStream(__das,
				len))
			{
				// Only constants are valid
				if (!kind.equals("ConstantValue"))
					continue;
				
				try (DataInputStream dais = new DataInputStream(bais))
				{
					// {@squirreljme.error CF1r Field already has a constant
					// value. (The field ID)}
					if (constval != null)
						throw new NCIException(NCIException.Issue.DOUBLE_CONST,
							String.format("CF1r", id));
					
					// Reads the constant field value
					constval = __oc.constantPool().<NCIConstantValue>
						requiredAs(dais.readUnsignedShort(),
						NCIConstantValue.class).get();
				}
			}
		}
		
		// {@squirreljme.error CF1s Duplicate field in class. (The field ID)}
		NCIField old = __into.put(id, new NCFField(__oc,
			id, __FlagDecoder__.__field(__oc, flags), constval));
		if (old != null)
			throw new NCIException(NCIException.Issue.DUPLICATE_FIELD,
				String.format("CF1s", id));
	}
	
	/**
	 * Reads a method from the given class.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @param __into The target mappings.
	 * @throws IOException On read errors.
	 * @sicne 2016/04/26
	 */
	static void __method(NCFClass __oc, DataInputStream __das,
		Map<NCIMethodID, NCIMethod> __into)
		throws IOException
	{
		// Read flags
		int flags = __das.readUnsignedShort();
		
		// Read name and type
		IdentifierSymbol name = __readIdentifier(__oc, __das);
		MethodSymbol type = __readType(__oc, __das).asMethod();
		
		// Setup ID
		NCIMethodID id = new NCIMethodID(name, type);
		
		// Read in attributes
		byte[] code = null;
		int na = __das.readUnsignedShort();
		for (int i = 0; i < na; i++)
		{
			// Read kind
			String kind = __oc.constantPool().<NCIUTF>requiredAs(
				__das.readUnsignedShort(), NCIUTF.class).toString();
			int len = __das.readInt();
			
			// {@squirreljme.error CF1w Method attribute has negative length.
			// (The method id)}
			if (len < 0)
				throw new NCIException(NCIException.Issue.NEGATIVE_ATTRIBUTE,
					String.format("CF1w", id));
			
			// Setup area
			try (BufferAreaInputStream bais = new BufferAreaInputStream(__das,
				len))
			{
				// Only code is valid
				if (!kind.equals("Code"))
					continue;
				
				try (DataInputStream dais = new DataInputStream(bais))
				{
					// {@squirreljme.error CF1v Method already has a code
					// attribute. (The method ID)}
					if (code != null)
						throw new NCIException(NCIException.Issue.DOUBLE_CODE,
							String.format("CF1v", id));
					
					// {@squirreljme.error CF1v Did not read the entire code
					// attribute for a method. (The method ID)}
					code = new byte[len];
					if (len != dais.read(code))
						throw new NCIException(NCIException.Issue.SHORT_CODE,
							String.format("CF1w", id));
				}
			}
		}
		
		// {@squirreljme.error CF1u Duplicate field in class. (The field ID)}
		NCIMethod old = __into.put(id, new NCFMethod(__oc,
			id, __FlagDecoder__.__method(__oc, flags), code));
		if (old != null)
			throw new NCIException(NCIException.Issue.DUPLICATE_METHOD,
				String.format("CF1u", id));
	}
	
	/**
	 * Reads the identifier of a member from the input stream.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @return The read identifier.
	 * @throws IOException On read errors.
	 * @since 2016/04/26
	 */
	private static IdentifierSymbol __readIdentifier(NCFClass __oc,
		DataInputStream __das)
		throws IOException
	{
		return __oc.constantPool().<NCIUTF>requiredAs(
			__das.readUnsignedShort(), NCIUTF.class).asIdentifier();
	}
	
	/**
	 * Reads the type of a member from the input stream.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @return The read type.
	 * @throws IOException On read errors.
	 * @since 2016/04/26
	 */
	private static MemberTypeSymbol __readType(NCFClass __oc,
		DataInputStream __das)
		throws IOException
	{
		return __oc.constantPool().<NCIUTF>requiredAs(
			__das.readUnsignedShort(), NCIUTF.class).asMember();
	}
	
	/**
	 * Skips the given amount of bytes.
	 *
	 * @param __das The input data source.
	 * @param __len The number of bytes to skip.
	 * @throws IOException On read errors.
	 * @since 2016/04/26
	 */
	static void __skipBytes(DataInputStream __das, int __len)
		throws IOException
	{
		for (int total = 0; total != __len;)
		{
			// Skip bytes
			int act = (int)__das.skip(__len - total);
			
			// Calculate new total
			int nt = total + act;
			
			// {@squirreljme.error CF1p End of file reached while skipping
			// the class attributes.}
			if (total >= nt)
				throw new NCIException(NCIException.Issue.READ_ERROR,
					"CF1p");
			
			// Set new total
			total = nt;
		}
	}
}

