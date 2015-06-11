// Agent sample_agent in project agentInspectionMachine
/* Initial beliefs and rules */
/* Initial goals */
!start.
/* Plans */
+!start : true <- .print("Start system");
	.wait(board(_));
	//!!check_board
	.
+!check_board : board(B) & B = 1 <-
	+ok; 
	!check_board;
	.
+!check_board : board(B) & B = 0 <- .print("Finish Inspection Board");
	//.drop_desire(inspection); 
	-ok;
	!start;
	.
+parameters(_,_,_,_,_) : true <- !test.
+!test : parameters(L, C, T, A, D) & T = 1 & C = 1 & A = 1 & D = 1 <- .print("componente ok");
	//.send(A, tell, "Ok");
	send_action(1);
	-parameters(L,C,T,A,D);
	-check(_);
	-ok;
	-board;
	.print("Wait board");
	.
+!test : parameters(L,C, T, A, D) & (C = 0 | T = 0 | A = 0 | D = 0) <- .print("componente Nok");
	//.send(A, tell, "NOk");
		send_action(0);
	-parameters(L,C, T, A, D);
	-check(_);
	-ok;
	-board;
	.print("Wait board");
	.
