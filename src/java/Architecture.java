import java.util.ArrayList;
import java.util.List;

import jason.NoValueException;
import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;


public class Architecture extends AgArch {
	
Communication connectionSystem = new Communication();
	
	@Override
	public void init(){
		connectionSystem.start();
	}

	@Override
	public List<Literal> perceive() {

		//get default perceptions
		List<Literal> per = super.perceive();
		
		if(connectionSystem.getCheck() != null) {
			// parameters(line ,type, angle, distance)	
			Literal L1 = ASSyntax.createLiteral("parameters",
					ASSyntax.createNumber(connectionSystem.getLine()),
					ASSyntax.createNumber(connectionSystem.getCheck()), // valor de check (exists)
					ASSyntax.createNumber(connectionSystem.isType()), // valor de "type"
					ASSyntax.createNumber(connectionSystem.isAngle()), // valor de "angle"
					ASSyntax.createNumber(connectionSystem.isDistance())); // valor de "distance"

		
			Literal L2 = ASSyntax.createLiteral("board", ASSyntax.createNumber(connectionSystem.getBoard()));

			if (per==null) {
				per = new ArrayList<Literal>();
			}

			per.add(L1);
			per.add(L2);
		
		}

		return per;

	}
	
	@Override
	public void act (ActionExec action, List<ActionExec> feedback ) {
		
		String afunctor = action.getActionTerm().getFunctor();
		
		if (afunctor.equals("send_report")) {
			try {
				//System.out.println("% Interface: Send Report %");
				connectionSystem.setLineCheck(((NumberTerm)(action.getActionTerm().getTerm(0))).solve());
				connectionSystem.setK(true); // check para envio da mesnsagem com a nova trajetória
			} catch (NoValueException e1) {
				e1.printStackTrace();
			}
			
			action.setResult(true);
			feedback.add(action);
		
		} else {
			super.act(action, feedback);
		}
	}
}
