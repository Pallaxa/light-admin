package org.lightadmin.core.config.domain.field;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class FieldMetadataUtils {

	public static Set<FieldMetadata> extractFields( Iterable<FilterMetadata> filterMetadatas ) {
		return newLinkedHashSet( Collections2.transform( newLinkedHashSet(filterMetadatas), new FieldMetadataExtractor() ) );
	}

	public static Predicate<FieldMetadata> persistentFieldMetadataPredicate() {
		return new FieldMetadataTypePredicate( PersistentFieldMetadata.class );
	}

	public static Predicate<FieldMetadata> transientFieldMetadataPredicate() {
		return new FieldMetadataTypePredicate( TransientFieldMetadata.class );
	}

	public static Predicate<FieldMetadata> customFieldMetadataPredicate() {
		return new FieldMetadataTypePredicate( CustomFieldMetadata.class );
	}

	public static Set<FieldMetadata> selectFields( Set<FieldMetadata> fieldMetadatas, Predicate<FieldMetadata>... predicates ) {
		return newLinkedHashSet( Collections2.filter( fieldMetadatas, Predicates.and( predicates ) ) );
	}

	public static Set<FieldMetadata> persistentFields( Set<FieldMetadata> fieldMetadatas ) {
		return newLinkedHashSet( Collections2.filter( fieldMetadatas, persistentFieldMetadataPredicate() ) );
	}

	public static Set<FieldMetadata> transientFields( Set<FieldMetadata> fieldMetadatas ) {
		return newLinkedHashSet( Collections2.filter( fieldMetadatas, transientFieldMetadataPredicate() ) );
	}

	public static Set<FieldMetadata> customFields( Set<FieldMetadata> fieldMetadatas ) {
		return newLinkedHashSet( Collections2.filter( fieldMetadatas, customFieldMetadataPredicate() ) );
	}

	private static class FieldMetadataExtractor implements Function<FilterMetadata, FieldMetadata> {

		@Override
		public FieldMetadata apply( @Nullable final FilterMetadata filterMetadata ) {
			return filterMetadata.getFieldMetadata();
		}
	}

	private static class FieldMetadataTypePredicate implements Predicate<FieldMetadata> {

		private final Class<? extends FieldMetadata> fieldMetadataClass;

		private FieldMetadataTypePredicate( final Class<? extends FieldMetadata> fieldMetadataClass ) {
			this.fieldMetadataClass = fieldMetadataClass;
		}

		@Override
		public boolean apply( @Nullable final FieldMetadata fieldMetadata ) {
			return ClassUtils.isAssignableValue( fieldMetadataClass, fieldMetadata );
		}
	}
}