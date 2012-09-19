package fr.opensagres.xdocreport.play.modules.old;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import play.exceptions.UnexpectedException;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.results.Result;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Classe de génération de la réponse http au format d'un document ODT.
 * Renvoie une réponse "200 OK" avec un contenu de type "text/odt".
 *
 * @author Benoît Courtine.
 * @since 14/10/2010.
 */
public class XdocReportResult extends Result {

	/** Nom du document ODT généré. */
	String fileName;
	/** Template du document ODT. */
	IXDocReport report ;
	/** Données à fusionner dans le template. */
	Map<String, Object> data;

	/**
	 * Création du RenderOdt, à partir nom du modèle (qui servira à nommer le fichier téléchargé), du template ODT
	 * chargé ainsi que de la {@link java.util.Map} des paramètres qui vont être fusionnés dans celui-ci par la
	 * librairie JodReports.
	 *
	 * @param fileName Nom du fichier qui sera téléchargé.
	 * @param template Template de document ODT.
	 * @param data Données permettant la fusion du document final.
	 */
	public XdocReportResult(String fileName, IXDocReport report, Map<String, Object> data) {
		this.fileName = fileName;
		this.report=report;
		this.data = data;
	}

	/**
	 * Génération du document et écriture de celui-ci dans le flux de réponse.
	 *
	 * @param request Objet requête.
	 * @param response Objet réponse.
	 */
	@Override
	public void apply(Request request, Response response) {

		try {
			// Nom du document généré.
			response.setHeader("Content-Disposition", "inline; filename=\""+ fileName +"\"");
			// Type mime des documents ODT.
			setContentTypeIfNotSet(response, "application/vnd.oasis.opendocument.text");
			// Fusion du document envoyé dans le flux de réponse.
			//template.createDocument(data, response.out);
			 IContext context = report.createContext();
			 for (Map.Entry<String, Object> element : data.entrySet()) {
				 context.put(element.getKey(), element.getValue());
			}
		      //Project project = new Project("XDocReport");
		    //  context.put("project", project);


			report.process(context, response.out);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
	}
}
