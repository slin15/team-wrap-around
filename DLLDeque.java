import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Deque; //Does not conform to the standard library because of unimplemented methods in subinterfaces

public class DLLDeque<E> implements MyDeque<E>, Iterable {
    DLLNode<E> _head, _tail;
    int _size;
    int _max;

    public DLLDeque() {
       	_size = 0;
       	_max = -1;
    }

    public DLLDeque(int max) {
       	this();
       	_max = max;
    }
	
    public boolean add(E e) {
	addLast(e);
	return true;
    }
	
    public void addFirst(E e) {
	if( (_max >= 0) && (_size >= _max) ) {
	    throw new IllegalStateException("Max size reached");
	}
	else if(size() == 0) {
	    DLLNode<E> newNode = new DLLNode<E>(e, null, null);
	    _head = newNode;
	    _tail = newNode;
	}
	else {
	    DLLNode<E> newNode = new DLLNode<E>(e, null, _head);
	    _head.setPrev(newNode);
	    _head = newNode;
	}
	_size++;
    }
	
    public void addLast(E e) {
	if( (_max >= 0) && (_size >= _max) ) {
	    throw new IllegalStateException("Max size reached");
	}
	else if(size() == 0) {
	    DLLNode<E> newNode = new DLLNode<E>(e, null, null);
	    _head = newNode;
	    _tail = newNode;
	}
	else {
	    DLLNode<E> newNode = new DLLNode<E>(e, _tail, null);
	    _tail.setNext(newNode);
	    _tail = newNode;
	}
	_size++;
    }

    public void push(E e) {
	addFirst(e);
    }
	
    public boolean offer(E e) {
	return offerLast(e);
    }
	
    public boolean offerFirst(E e) {
	if( (_max >= 0) && (_size >= _max) ) {
	    return false;
	}
	addFirst(e);
	return true;
    }
	
    public boolean offerLast(E e) {
	if( (_max >= 0) && (_size >= _max) ) {
	    return false;
	}
	addLast(e);
	return true;
    }
	
    public E remove(){
	return removeFirst();
    }
	
    public E removeFirst(){
	E retVal;
	if(size() == 0) { throw new NoSuchElementException();}
	if(size() == 1) {
	    retVal = _head.getValue();
	    _head = null;
	    _tail = null;
	}
	else {
	    retVal = _head.getValue();
	    _head = _head.getNext();
	    _head.setPrev(null);
	}
	_size--;
	return retVal;
    }
    
    public E removeLast(){
	E retVal;
	if(size() == 0) { throw new NoSuchElementException();}
	if(size() == 1) {
	    retVal = _head.getValue();
	    _head = null;
	    _tail = null;
	}
	else {
	    retVal = _tail.getValue();
	    _tail = _tail.getPrev();
	    _tail.setNext(null);
	}
	_size--;
	return retVal;
    }

    public boolean remove(Object o) {
	return removeFirstOccurrence(o);
    }
    
    public boolean removeFirstOccurrence(Object o) {
	Iterator<E> iter = iterator();
	while(iter.hasNext()) {
	    E e = iter.next();
	    if(e.equals(o)) {
		iter.remove();
		return true;
	    }
	}
	return false;
    }

    public boolean removeLastOccurrence(Object o) {
	Iterator<E> iter = descendingIterator();
	while(iter.hasNext()) {
	    E e = iter.next();
	    if(e.equals(o)) {
		iter.remove();
		return true;
	    }
	}
	return false;
    }
    
    public E pop() {
	return removeFirst();
    }
	
    public E poll() {
	return pollFirst();
    }
	
    public E pollFirst() {
	if(_size == 0) { return null; }
	return removeFirst();
    }
	
    public E pollLast() {
	if(_size == 0) { return null; }
	return removeLast();
    }
	
    public int size() {
       	return _size;
    }
	
    public E getFirst() {
	if( _size == 0 ) { throw new NoSuchElementException(); }
       	return _head.getValue();
    }

    public E getLast() {
	if( _size == 0 ) { throw new NoSuchElementException(); }
	return _tail.getValue();
    }
	
    public E peek() {
	return peekFirst();
    }
	
    public E peekFirst() {
	if( _size == 0 ) { return null; }
	return getFirst();
    }
	
    public E peekLast() {
	if( _size == 0 ) { return null; }
	return getLast();
    }
	
    public E element() {
	return getFirst();
    }

    public Iterator<E> iterator() {
	return new DLLDequeIterator(true);
    }

    public Iterator<E> descendingIterator() {
	return new DLLDequeIterator(false);
    }

    public boolean contains(Object o) {
	Iterator<E> i = this.iterator();
	while (i.hasNext()) {
	    E e = i.next();
	    if( e.equals(o) ) {
		return true;
	    }
	}
	return false;
    }
    
    public String toString() {
	if( _size == 0 ) {return null;}
	String retStr = "HEAD->";
	DLLNode tmp = _head;
	for(int i = 0; i < _size; i++) {
	    retStr += tmp.getValue() + "<->";
	    tmp = tmp.getNext();
	}
	retStr = retStr.substring(0, retStr.length()-3);
	return retStr + "<-TAIL";
    }

    /*****************************************************
     * inner class MyIterator
     * Adheres to specifications given by Iterator interface.
     * Uses dummy node to facilitate iterability over DLLDeque.
     *****************************************************/
    private class DLLDequeIterator implements Iterator<E> 
    {
	private DLLNode<E> _dummy;   // dummy node to tracking pos
	private boolean _okToRemove; // flag indicates next() was called
	private boolean head; //Whether the iterator starts from the head or the tail
	
	//constructor 
	public DLLDequeIterator(boolean head) 
	{
	    this.head = head;
	    //place dummy node in front of head
	    if(head)
		_dummy = new DLLNode<E>( null, null , _head);
	    else
		_dummy = new DLLNode<E>( null, _tail, null );
	    _okToRemove = false;
	}

	//-----------------------------------------------------------
	//--------------v  Iterator interface methods  v-------------
	//return true if iteration has more elements.
	public boolean hasNext() 
	{
	    if(head)
		return _dummy.getNext() != null;
	    else
		return _dummy.getPrev() != null;
	}


	//return next element in this iteration
	public E next() 
	{
	    if(head)
		_dummy = _dummy.getNext();
	    else
		_dummy = _dummy.getPrev();
	    if ( _dummy == null )
		throw new NoSuchElementException();
	    _okToRemove = true;
	    return _dummy.getValue();
	}


	//return last element returned by this iterator (from last next() call)
	//postcondition: maintains invariant that _dummy always points to a node
	//               (...so that hasNext() will not crash)
	public void remove() 
	{
	    if ( ! _okToRemove )
		throw new IllegalStateException("must call next() beforehand");
	    _okToRemove = false;

	    //If removing only remaining node...
	    //maintain invariant that _dummy always points to a node
	    //   (...so that hasNext() will not crash)
	    if ( _head == _tail ) {
		_head = _tail = null;
	    }
	    //if removing first node...
	    else if ( _head == _dummy ) {
		_head = _head.getNext();
		_head.setPrev( null ); //just to save mem
	    }
	    //if removing last node...
	    else if ( _tail == _dummy ) {
		_tail = _tail.getPrev();
		_tail.setNext( null );
	    }
	    //if removing an interior node...
	    else {
		_dummy.getNext().setPrev( _dummy.getPrev() );
		_dummy.getPrev().setNext( _dummy.getNext() );
	    }

	    _size--; //decrement size attribute of outer class LList      
	}//end remove()
	//--------------^  Iterator interface methods  ^-------------
	//-----------------------------------------------------------
    }//*************** end inner class MyIterator *************** 

    public static void main( String[] args ) {
	DLLDeque<String> test = new DLLDeque<>();
	//Adding test
	System.out.println(test); //null
	test.addFirst("foo");
	System.out.println(test); //foo
	test.addFirst("bar");
	System.out.println(test); //bar, foo
	test.addLast("baf");
	System.out.println(test); //bar, foo, baf
	
	//Iterator testing
	Iterator<String> iter = test.iterator();
	System.out.println(iter.next()); //bar
	System.out.println(iter.next()); //foo
        //iter.remove();
	//System.out.println(test);
	System.out.println(iter.next()); //baf

	//Descending iterator testing
        iter = test.descendingIterator();
	System.out.println(iter.next()); //baf
	System.out.println(iter.next()); //foo
        //iter.remove();
	//System.out.println(test);
	System.out.println(iter.next()); //bar

	//Contains test
	System.out.println(test.contains("baf"));//true
	System.out.println(test.contains("baaar"));//false

	//Remove test
	System.out.println(test.remove("baf"));//true
	System.out.println(test.remove("baaar"));//false
	System.out.println(test);
	test.addLast("baf");
	System.out.println(test);
	
	//Size test
	System.out.println(test.size()); //3 

	//Accessor test 
	System.out.println(test.getFirst()); //bar
	System.out.println(test.getLast()); //baf

	//Remove
	test.removeFirst();
	System.out.println(test); //foo, baf
	test.removeLast();
	System.out.println(test); //foo
    }
}
