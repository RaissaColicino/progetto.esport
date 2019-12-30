package controller.gestioneCatalogo;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.ProdottoBean;
import beans.RecensioneBean;
import topdown.ProdottoModelStub;
import topdown.RecensioneModelStub;

@WebServlet("/SchedaProdotto")
public class SchedaProdotto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger log=Logger.getLogger("SchedaProdottoDebugger");
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		
		synchronized(session) {
			log.info("Scheda prodotto -> ottengo il codice del prodotto da mostrare dalla richiesta");
			String codiceProdotto=request.getParameter("codProd");
			
			ProdottoModelStub prodottoModel=new ProdottoModelStub();
			
			log.info("Scheda prodotto -> ottengo il prodotto da mostrare");
			ProdottoBean prodottoDaMostrare=prodottoModel.doRetrieveByCodice(codiceProdotto);
			if(prodottoDaMostrare!=null) {				
				log.info("Scheda prodotto -> ottengo le recensioni per il prodotto da mostrare");
				RecensioneModelStub recensioneModel=new RecensioneModelStub();
				LinkedHashSet<RecensioneBean> recensioni=(LinkedHashSet<RecensioneBean>) recensioneModel.doRetrieveAll();
				if(recensioni.size()!=0) {
					prodottoDaMostrare.setRecensioni(recensioneModel.doRetrieveByProdotto(codiceProdotto));
				}	
				
				log.info("Scheda prodotto -> aggiungo il prodotto da mostrare alla sessione");
				session.setAttribute("ProdottoDaMostrare", prodottoDaMostrare);
			}
		}
		
		RequestDispatcher view=request.getRequestDispatcher("SchedaProdotto.jsp");
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
