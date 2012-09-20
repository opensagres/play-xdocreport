package controllers;

import static fr.opensagres.xdocreport.play.modules.XDocReportModule.generateReport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import models.Personne;
import play.Play;
import play.mvc.Controller;
import play.mvc.Scope;
import play.mvc.results.Result;
import play.vfs.VirtualFile;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;

/**
 * Contrôleur de démonstration, utilisant l'import statique des méthodes de
 * {@link play.modules.odt.OdtRenderer}.
 */
public class Application extends Controller {


	static {
		List<VirtualFile> templates = Play.templatesPath;

		for (VirtualFile virtualFile : templates) {

			doDiscoverReport(virtualFile);

		}
	}

	private static void doDiscoverReport(VirtualFile virtualFile) {
		if (!virtualFile.isDirectory()) {
			try {
				XDocReportRegistry.getRegistry().loadReport(virtualFile.inputstream(),virtualFile.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XDocReportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<VirtualFile> children = virtualFile.list();
			for (VirtualFile virtualFile2 : children) {
				System.out.println(virtualFile2.getRealFile());
				doDiscoverReport(virtualFile2);
			}
		}
	}
	/**
	 * Page d'accueil : guide de l'applicaion de démonstration.
	 */
	public static void index() {
		render();
//		if (!initialized) {
//
//			try {
//				List<VirtualFile> templates = Play.templatesPath;
//
//				for (VirtualFile virtualFile : templates) {
//					if (!virtualFile.isDirectory()) {
//						try {
//							XDocReportRegistry.getRegistry().loadReport(virtualFile.inputstream(),virtualFile.getName());
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (XDocReportException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//				}
//			} finally {
//				initialized = true;
//			}
//		}
	}

	/**
	 * Génération d'un document de démonstration, contenant uniquement une
	 * variable simple de type texte.
	 */
	public static void demoSimple() {
		String titre = "Document de démonstration COUCOU";


		IXDocReport report = XDocReportRegistry.getRegistry().getReport("demoSimple.odt");

		report.setTemplateEngine(new FreemarkerTemplateEngine());
		IContext context;
		try {
			context = report.createContext();
			context.put("titre", titre);

			   ByteArrayOutputStream out = new ByteArrayOutputStream();
			//Result res= generateReport(report, context);

			   report.process(context, out);

InputStream is = new ByteArrayInputStream(out.toByteArray());

renderBinary(is,"demoSimple.odt");


		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// renderDocument(titre);
	}

	/**
	 * Génération d'un document de démonstration, affichant plusieurs
	 * propriétés d"un unique objet {@link models.Personne}.
	 */
	public static void demoObjet() {
		Personne utilisateur = new Personne("Dupont", "Henry", 32);
		IXDocReport report = XDocReportRegistry.getRegistry().getReport(
				"demoObjet");
		IContext context;
		try {
			context = report.createContext();
			context.put("utilisateur", utilisateur);
			throw generateReport(report, context);
		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO renderDocument(utilisateur);
	}

	/**
	 * Génération d'un document de démonstration, affichant une valeur
	 * sauvegardée en session.
	 */
	public static void demoSession() {
		Scope.Session.current().put("fonction", "Administrateur");
		IXDocReport report = XDocReportRegistry.getRegistry().getReport(
				"demoSession");
		IContext context;
		try {
			context = report.createContext();
			Map<String, String> sessionScope = Scope.Session.current().all();
			for (Map.Entry<String, String> entry : sessionScope.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

			throw generateReport(report, context);
		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Génération d'un document de démonstration, affichant une valeur
	 * passée en paramètre de la requête HTTP.
	 */
	public static void demoRequete() {
		// Récupération du numéro de dossier de la requête, "Inconnu" si non
		// spécifié (pour assurer le bon
		// fonctionnement de la génération du document qui ne permet pas les
		// paramètres non renseignés).
		if (!Scope.Params.current()._contains("numDossier")) {
			Scope.Params.current().put("numDossier", "Inconnu");
		}
		IXDocReport report = XDocReportRegistry.getRegistry().getReport(
				"demoRequete");
		IContext context;
		try {
			context = report.createContext();

			Map<String, String[]> sessionScope = Scope.Params.current().all();
			for (Map.Entry<String, String[]> entry : sessionScope.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

			throw generateReport(report, context);
		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Génération d'un document de démonstration, affichant une liste de
	 * prénoms.
	 */
	public static void demoListe() {
		List<String> prenoms = new ArrayList<String>();
		prenoms.add("Thomas");
		prenoms.add("Stéphanie");
		prenoms.add("Jules");
		prenoms.add("Virginie");
		IXDocReport report = XDocReportRegistry.getRegistry().getReport(
				"demoRequete");
		IContext context;
		try {
			context = report.createContext();

			context.put("prenoms", prenoms);

			throw generateReport(report, context);
		} catch (XDocReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
