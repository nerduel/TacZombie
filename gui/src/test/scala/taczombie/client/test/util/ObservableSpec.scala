package taczombie.client.test.util

import org.specs2.mutable.Specification
import taczombie.client.util.Observable
import taczombie.client.util.Observer

class ObservableSpec extends Specification {
	
  class TestObservable extends Observable
  
  class TestObserver extends Observer {
    var updateWasCalled = false
    def update {
      updateWasCalled = true
    }
  }
	
  "class Observer" should {
	  
    "be in Subscriber List after add'" in {
      val observable = new TestObservable
      val observer = new TestObserver
      
      observable.add(observer)
      
      observable.subscribers.contains(observer) must be_== (true)
    }
    
    "be not in Subscriber List after add and remove'" in {
      val observable = new TestObservable
      val observer = new TestObserver
      
      observable.add(observer)
      observable.remove(observer)
      
      observable.subscribers.contains(observer) must be_== (false)
    }
  }
  
  "Method updated from Observer" should {
	    
    "not be called when Observable didnt call notifyObservers'" in {
      val observable = new TestObservable
      val observer = new TestObserver
      
      observable.add(observer)
      
      observer.updateWasCalled must be_== (false)
    }
    
    "be called when Observable calls notifyObservers'" in {
      val observable = new TestObservable
      val observer = new TestObserver
      
      observable.add(observer)
      observable.notifyObservers
      
      observer.updateWasCalled must be_== (true)
    }
  }
}