package fr.opensagres.xdocreport.play.modules.old;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import play.exceptions.PlayException;
import play.exceptions.TemplateCompilationException;
import play.templates.Template;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

/**
 * Template ODT étendant les templates Play.
 *
 * @author Benoît Courtine.
 * @since 24/10/2010.
 */
public class XDocReportTemplate extends Template {



	/**
	 * Registry XdocReport.
	 */
	private static final XDocReportRegistry REGISTRY = XDocReportRegistry.getRegistry();
	/** Final name of the generated file. */
	private String fileName;

	private String templateName;

	/** Template XdocReport. */
	private IXDocReport documentTemplate;

	/**
	 * Création du template en trouvant le nom du fichier à partir de son nom.
	 *
	 * @param templateName Nom du template.
	 */
	public XDocReportTemplate(String templateName) {
		this.templateName=templateName;

	}


	/**
	 * Création du template en trouvant le nom du fichier à partir de son nom.
	 *
	 * @param templateName Nom du template.
	 */
	public XDocReportTemplate(String templateName,TemplateEngineKind templateEngineKind) {
		this.templateName=templateName;
	}



	/**
	 * Compilation du template ODT en {@link net.sf.jooreports.templates.DocumentTemplate} prêt à être fusionné.
	 */

	public void compile() {

			documentTemplate=REGISTRY.getReport(templateName);
			if(!documentTemplate.isPreprocessed()){
				try {
					documentTemplate.preprocess();
				} catch (XDocReportException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


	}


	protected String internalRender(Map<String, Object> args) {
		compile();
		throw new XdocReportResult(this.fileName, this.documentTemplate, args);
	}


	@Override
	public String render(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
