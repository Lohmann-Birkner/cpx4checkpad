/**
 * License note to be added here.
 */
 
${pojo.getPackageDeclaration()}

<#assign classbody>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>/**
 * Data access object for domain model class ${declarationName}.
 * Initially generated at ${date} by Hibernate Tools ${version}.
 * @author Hibernate Tools
 */
@Stateless
public class ${declarationName}Dao extends AbstractCommonDao<${declarationName}>{

	/**
	 * Creates a new instance.
	 */ 
	public ${declarationName}Dao(){
		super(${declarationName}.class);
	}
}	
</#assign>

${pojo.generateImports()}
${classbody}