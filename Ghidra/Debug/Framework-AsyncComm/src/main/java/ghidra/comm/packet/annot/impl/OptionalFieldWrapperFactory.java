/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.comm.packet.annot.impl;

import java.lang.reflect.Field;
import java.util.Deque;

import ghidra.comm.packet.Packet;
import ghidra.comm.packet.annot.OptionalField;
import ghidra.comm.packet.codecs.PacketCodecInternal;
import ghidra.comm.packet.err.PacketAnnotationException;
import ghidra.comm.packet.fields.*;

/**
 * The factory implementing {@link OptionalField}
 *
 * @param <ENCBUF> the type of temporary buffer used to encode
 * @param <DECBUF> the type of temporary buffer used to decode
 */
public class OptionalFieldWrapperFactory<ENCBUF, DECBUF>
		extends AbstractWrapperFactory<ENCBUF, DECBUF> {
	@Override
	public <T> FieldCodec<ENCBUF, DECBUF, T> getWrappedFieldCodecForType(
			Class<? extends Packet> pktType, Field field, Class<? extends T> fldType,
			PacketCodecInternal<?, ENCBUF, DECBUF> codec,
			Deque<FieldCodecFactory<ENCBUF, DECBUF>> chain) {
		OptionalField annot = field.getAnnotation(OptionalField.class);
		if (fldType.isPrimitive()) {
			throw new PacketAnnotationException(pktType, field, annot,
				"Field cannot be primitive (must accept null)");
		}

		FieldCodec<ENCBUF, DECBUF, T> chainNext =
			FieldCodecFactory.buildChainForType(pktType, field, fldType, codec, chain);
		return new OptionalFieldFieldCodec<>(pktType, field, codec, chainNext);
	}
}