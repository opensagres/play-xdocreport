package fr.opensagres.xdocreport.play.modules.old;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;

import play.Play;
import play.classloading.enhancers.LocalvariablesNamesEnhancer;
import play.mvc.Http;
import play.mvc.Scope;
import play.templates.Template;
import play.vfs.VirtualFile;

/**
 *
 * @author Pascal Leclercq.
 * @author Benoît Courtine.
 * @since 14/10/2010.
 */
public class XdocReportRenderer {

	/**
	 * Registry XdocReport.
	 */
	private static final XDocReportRegistry REGISTRY = XDocReportRegistry
			.getRegistry();

	public static void init() throws IOException, XDocReportException {

		for (VirtualFile vf : Play.templatesPath) {

			List<VirtualFile> templates = vf.list();
			for (VirtualFile virtualFile : templates) {
				REGISTRY.loadReport(virtualFile.inputstream(),virtualFile.getName());
			}

		}

	}



	/**
	 * Render the corresponding template
	 *
	 * @param args
	 *            The template data
	 */
	public static void renderDocument(String templateName,
			Object... args) {
		// String templateName;
		// if (args.length > 0 && args[0] instanceof String &&
		// LocalvariablesNamesEnhancer.LocalVariablesNamesTracer.getAllLocalVariableNames(args[0]).isEmpty())
		// {
		// templateName = args[0].toString();
		// } else {
		// templateName = getXdocReportTemplateName();
		// }

		renderTemplaterenderDocument(templateName, args);
	}

	/**
	 * Lookup for the templateId to use.
	 *
	 * @return Nom du template ODT à utiliser.
	 */
	protected static String getXdocReportTemplateName() {
		final Http.Request request = Http.Request.current();
		//String templateName = (String) request.get().args.get("templateId");
		// String templateName = request.action.replace(".", "/") + ".odt";
		// if (templateName.startsWith("@")) {
		// templateName = templateName.substring(1);
		// if (!templateName.contains(".")) {
		// templateName = request.controller + "." + templateName;
		// }
		// templateName = templateName.replace(".", "/") + ".odt";
		// }
		return "TODO";
	}

	/**
	 * Création du template ODT, récupération des paramètres à passer au
	 * template, et génération du rendu final.
	 *
	 * @param templateName
	 *            Nom du template à utiliser.
	 * @param args
	 *            Données à passer en paramètre du template.
	 */

	protected static void renderTemplaterenderDocument(String templateName, Object... args) {
		Template template = new XDocReportTemplate(templateName);
		Map<String, Object> templateBinding = getTemplateParams(args);
		template.render(templateBinding);
	}

	/**
	 * Création et enrichissement des paramètres permettant le rendu ODT.
	 * <p/>
	 * Le paramètre "args" est bien utilisé dans cette méthode, par l'injection
	 * de code effectuée par
	 * {@link play.classloading.enhancers.LocalvariablesNamesEnhancer}.
	 *
	 * @param args
	 *            Données passées en paramètre de la méthode de rendering.
	 * @return Map des données pour la fusion avec le template ODT.
	 */
	protected static Map<String, Object> getTemplateParams(Object... args) {
		// Template datas
		Map<String, Object> templateParams = Scope.RenderArgs.current().data;
		// Ajout des paramètres de la méthode.
		templateParams
				.putAll(LocalvariablesNamesEnhancer.LocalVariablesNamesTracer
						.getLocalVariables());
		// Ajout des données de session à la Map des informations permettant la
		// création du rendu final.
		templateParams.put("session", Scope.Session.current().all());
		// Ajout des paramètres d'appel à la Map des informations permettant la
		// création du rendu final.
		templateParams.put("params", Scope.Params.current().all());

		return templateParams;
	}
}
