public class Junio {

    public int transition(int state, char symbol) {
	switch(state) {
	  case 0:
	      if(symbol == 'a') return 1;
	      if(symbol == 'b') return 2;
	      if(symbol == 'c') return 3;
	      return -1;
	  case 1:
	      if(symbol == 'b') return 4;
	      if(symbol == 'c') return 0;
	      return -1;
	  case 2:
	      if(symbol == 'a') return 0;
	      if(symbol == 'c') return 5;
	      return -1;
	  case 3:
	      if(symbol == 'a') return 6;
	      if(symbol == 'b') return 0;
	      return -1;
	  case 4:
	      if(symbol == 'b') return 1;
	      return -1;
	  case 5:
	      if(symbol == 'c') return 2;
	      return -1;
	  case 6:
	      if(symbol == 'a') return 3;
	      return -1;
	  default:
	      return -1;
	  }
    }

    public boolean isFinal(int state) {
	switch(state) {
	  case 0: return true;
	  case 1: return false;
	  case 2: return false;
	  case 3: return false;
	  case 4: return false;
	  case 5: return false;
	  case 6: return false;
	  default: return false;
	}
    }

}