import "./style.css";
import { interval, fromEvent, merge, zip, pipe, Subscription, Observable } from "rxjs";
import { map, filter, takeUntil, scan, flatMap, mergeMap, take } from "rxjs/operators";

// Keep track of the score inside a variable
var score = 0;
var HighScore = 0;

function main() {
  /**
   * Inside this function you will use the classes and functions from rx.js
   * to add visuals to the svg element in pong.html, animate them, and make them interactive.
   *
   * Study and complete the tasks in observable examples first to get ideas.
   *
   * Course Notes showing Asteroids in FRP: https://tgdwyer.github.io/asteroids/
   *
   * You will be marked on your functional programming style
   * as well as the functionality that you implement.
   *
   * Document your code!
   */

  /**
   * This is the view for your game to add and update your game elements.
   */

  
  const svg = document.querySelector("#svgCanvas") as SVGElement & HTMLElement;

  // Store all the constant value
  const Constant = {
    carWidth: 36,
    carHeight: 36,
    logWidth: 100,
    logHeight: 36,
    frogWidth: 36,
    frogHeight: 36,
    longLogHeight: 36,
    longLogWidth: 150
  } as const

  // Game state
  type State = Readonly<{
    time: number,
    gameOver: boolean,
    reached: boolean;
    frog: Frog,
    carLeft: Object[],
    carRight: Object[],
    logLeft: Object[],
    logRight: Object[],
    longLog: Object[],
    distinctArea: Object[]
  }>
  
  const initialFrog:Frog = {
    x: 288,
    y: 561,
    w: Constant.frogWidth,
    h: Constant.frogHeight
  }

  type Frog = Readonly<{
    x: number,
    y: number,
    w: number,
    h: number
  }>

  // all elements created are objects
  type Object = Readonly<{
    id: string,
    x: number,
    y: number,
    w: number,
    h: number
  }>


  /**
   * Function that creates element which has the same attributes into the svg
   * @param colNumber Number of elements created
   * @param obj Object array to store the elements
   * @param name Name of the element
   * @param X the x-axis position
   * @param Y the y-axis position
   * @param width Width of the element
   * @param height Height of the element
   * @returns Returns the created element
   */
  const createObject = (colNumber:number, obj:Object[], name:string, X:number, Y:number, width:number, height:number): Object[] => {
    if(colNumber === 0){
      return obj;
    } else {
      const newObj: Object = {
        id: name + colNumber,
        x: colNumber * X,
        y: Y,
        w: width,
        h: height
      }
      return createObject(colNumber -1, obj.concat([newObj]), name, X, Y, width, height)
    }
  }

 
  /**
   * Update the svg scene, 
   * append the previously created elements into svg,
   * @param state Current state of all objects
   */
  const updateView = (state:State)=> {

    state.carLeft.forEach((carState: Object)=> {
      const car = document.getElementById(carState.id);

      if(car === null){
        const newCar = document.createElementNS(svg.namespaceURI, "rect");
        Object.entries({
            id: carState.id,
            x: carState.x,
            y: carState.y,
            width: Constant.carWidth,
            height: Constant.carHeight,
            fill: "#95B3D7",
          }).forEach(([key, val]) => newCar.setAttribute(key, String(val)));
          svg.appendChild(newCar);
      }
      else {
        car.setAttribute("x", String(carState.x))
      }
    }) 

    state.carRight.forEach((carState: Object)=> {
      const car = document.getElementById(carState.id);

      if(car === null){
        const newCar = document.createElementNS(svg.namespaceURI, "rect");
        Object.entries({
            id: carState.id,
            x: carState.x,
            y: carState.y,
            width: Constant.carWidth,
            height: Constant.carHeight,
            fill: "#95B3D7",
          }).forEach(([key, val]) => newCar.setAttribute(key, String(val)));
          svg.appendChild(newCar);
      }
      else {
        car.setAttribute("x", String(carState.x))
      }
    }) 

    state.logLeft.forEach((logState: Object)=> {
      const log = document.getElementById(logState.id);

      if(log === null){
        const newLog = document.createElementNS(svg.namespaceURI, "rect");
        Object.entries({
            id: logState.id,
            x: logState.x,
            y: logState.y,
            width: Constant.logWidth,
            height: Constant.logHeight,
            fill: "#9B603A",
          }).forEach(([key, val]) => newLog.setAttribute(key, String(val)));
          svg.appendChild(newLog);
      }
      else {
        log.setAttribute("x", String(logState.x))
      }
    }) 

    state.longLog.forEach((logState: Object)=> {
      const log = document.getElementById(logState.id);

      if(log === null){
        const newLog = document.createElementNS(svg.namespaceURI, "rect");
        Object.entries({
            id: logState.id,
            x: logState.x,
            y: logState.y,
            width: Constant.longLogWidth,
            height: Constant.longLogHeight,
            fill: "#9B603A",
          }).forEach(([key, val]) => newLog.setAttribute(key, String(val)));
          svg.appendChild(newLog);
      }
      else {
        log.setAttribute("x", String(logState.x))
      }
    }) 

    state.logRight.forEach((logState: Object)=> {
      const log = document.getElementById(logState.id);

      if(log === null){
        const newLog = document.createElementNS(svg.namespaceURI, "rect");
        Object.entries({
            id: logState.id,
            x: logState.x,
            y: logState.y,
            width: Constant.logWidth,
            height: Constant.logHeight,
            fill: "#9B603A",
          }).forEach(([key, val]) => newLog.setAttribute(key, String(val)));
          svg.appendChild(newLog);
      }
      else {
        log.setAttribute("x", String(logState.x))
      }
    }) 

    // update the frog state
    const circle = document.getElementById("circle")!;
    circle.setAttribute('transform',
     `translate(${state.frog.x},${state.frog.y})`)
     circle.parentNode?.appendChild(circle);


    // if gameOver == true, unsubscribe 
    if(state.gameOver) {
  
      const checkHighScore = HighScore > score ? HighScore : score;
      HighScore = checkHighScore;
    
      //update highscore
      const TextHighScore = document.getElementById("highscore")!;
      TextHighScore.innerHTML = "High Score: " + HighScore
      const TextScore = document.getElementById("score")!;
      TextScore.innerHTML = "Score: 0" 
      score = 0;

      subscription.unsubscribe();

      // Game Over and restart the game by calling the main function
      const textNode = document.createTextNode("Game Over");
      document.body.appendChild(textNode);
      const btn = document.createElement("button");
      btn.innerHTML = "Restart";
      document.body.appendChild(btn);
      btn.onclick = function() {
        textNode.remove();
        btn.remove();
        return main()
      }
    }

    // if reached == true, unsubscribe
    if(state.reached) {
      score +=  10;

      subscription.unsubscribe();

      const TextScore = document.getElementById("score")!;
      TextScore.innerHTML = "Score:" + score

      const textNode = document.createTextNode("You Win!");
      document.body.appendChild(textNode);

      const btn = document.createElement("button");
      btn.innerHTML = "Restart";
      document.body.appendChild(btn);
      btn.onclick = function() {
        textNode.remove();
        btn.remove();
        return main();
      }
    }
  }



  const CanvasSize = 600

  // wrap the object position around edges of the screen 
  const wrap = (v:number) => 
      v < 0 ? v + CanvasSize : v > CanvasSize ? v - CanvasSize : v;

  // prevent the frog to move outside of the Canvas
  const fixed = (v: number) =>
      v > CanvasSize ? CanvasSize - 36 : v < 0 ? 0 : v; 

  // Five types of game state transitions
  class Tick { constructor(public readonly elapsed:number) {} }
  class Left { constructor(public readonly left:number) {} }
  class Right { constructor(public readonly right:number) {} }
  class Up { constructor(public readonly up:number) {} }
  class Down { constructor(public readonly down:number) {} }

  type Event = 'keydown' | 'keyup' | 'keyleft' | 'keyright'
  type Key = 'ArrowLeft' | 'ArrowRight' | 'ArrowUp' | 'ArrowDown'

  // Reference from Asteroids05
  const 
  gameClock = interval(15)
      .pipe(map(elapsed=>new Tick(elapsed))) ,

  // listen for the keyboard event
  keyObservable = <T>(e:Event, k:Key, result:()=>T)=>
    fromEvent<KeyboardEvent>(document,e)
        .pipe(
          filter(({key})=>key === k),
          filter(({repeat})=>!repeat),
          map(result)),
    startLeft = keyObservable('keydown','ArrowLeft',()=>new Left(-40)),
    startRight = keyObservable('keydown','ArrowRight',()=>new Right(40)),
    startUp = keyObservable('keyup','ArrowUp',()=>new Up(-40)),
    starttDown = keyObservable('keyup','ArrowDown',()=>new Down(40))


  // Record all the initial state and create the objects
  const
    initialState: State = {
    time: 0,
    gameOver: false,
    reached: false,
    frog: initialFrog,
    carLeft: createObject(3, [], "carLeft", (Constant.carWidth + 150), 521, Constant.carWidth, Constant.carHeight),
    carRight: createObject(3,[], "carRight", (Constant.carWidth + 180), 441, Constant.carWidth, Constant.carHeight),
    logLeft: createObject(3,[], "logRight", (Constant.logWidth + 300), 241, Constant.logWidth, Constant.logHeight),
    logRight: createObject(3,[], "logLeft", (Constant.logWidth + 300), 281, Constant.logWidth, Constant.logHeight),
    longLog: createObject(2,[], "longLog", (Constant.longLogWidth + 200), 201, Constant.longLogWidth, Constant.longLogHeight),
    distinctArea: createObject(5,[], "distinctArea", (120 + 48), 161, 40, 39)
  },

    // check for collision
    handleCollisions = (s:State) => {
    const 
      // check the object's positions for collision
      bodiesCollided = (c: Object) => 
        s.frog.x + s.frog.w >= c.x &&
        s.frog.x <= c.x + c.w &&
        s.frog.y + s.frog.h >= c.y &&
        s.frog.y <= c.y + c.h

      ,logLeftCollided = (s.logLeft.filter(r=>bodiesCollided(r)).length > 0) 
                              
      ,logRightCollided = (s.logRight.filter(r=>bodiesCollided(r)).length > 0) 
                            
      ,longLogCollided = (s.longLog.filter(r=>bodiesCollided(r)).length > 0) 

      // if frog collides with the log, move the frog along with the log by 
      // changing the frog's speed same as the log's speed
      ,checkLog = logLeftCollided ? {...s.frog, x:fixed(s.frog.x + 0.5)} 
                          : logRightCollided ? {...s.frog, x:fixed(s.frog.x - 0.3)} 
                          : longLogCollided ? {...s.frog, x:fixed(s.frog.x - 0.6)} : {...s.frog}
                    
      // check if frog contact with river
      ,contactRiver = s.frog.x + s.frog.w >= 0 &&
                      s.frog.x <= 0 + 600 &&
                      s.frog.y + s.frog.h >= 160 &&
                      s.frog.y <= 160 + 160

      // check if frog reached the distinct area
      ,reachedTarget = (s.distinctArea.filter(r=>bodiesCollided(r)).length > 0)

      // check if either the frog collide with obstacles or contact with the river
      ,frogCollided = (s.carLeft.filter(r=>bodiesCollided(r)).length > 0) 
                      || (s.carRight.filter(r=>bodiesCollided(r)).length > 0) 
                      || (contactRiver && !(logLeftCollided || logRightCollided || longLogCollided || reachedTarget))
          
    // update the state
    return <State>{
      ...s,
      gameOver: frogCollided,
      frog: checkLog,
      reached: reachedTarget
      }
    }
  

    // check for collision and also moves the objects
    // move the object by changing its attribute
    const tick = (s: State, elapsed:number) => {
      return handleCollisions({...s,
        time: elapsed,
        carLeft: s.carLeft.map((car:Object)=> {
          return {...car, x:wrap(car.x +0.8)}
        }),
        carRight: s.carRight.map((car:Object)=> {
          return {...car, x:wrap(car.x -0.7)}
        }),
        logLeft: s.logLeft.map((log:Object)=> {
          return {...log, x:wrap(log.x +0.5)}
        }),
        logRight: s.logRight.map((log:Object)=> {
          return {...log, x:wrap(log.x -0.3)}
        }),
        longLog: s.longLog.map((log:Object)=> {
          return {...log, x:wrap(log.x -0.6)}
        })
      })
    },
   
    // state transducer
    // move the frog by retrieveing the value from the class and add with the current state
    reduceState = (s:State, e:Left|Right|Up|Down|Tick)=>
      e instanceof Left ? {...s,
      frog:{...s.frog, x:fixed(s.frog.x + e.left)}
    } :
    e instanceof Right ? {...s,
      frog:{...s.frog, x:fixed(s.frog.x + e.right)}
    } : 
    e instanceof Up ? {...s,
      frog:{...s.frog, y:fixed(s.frog.y + e.up)}
    } : 
    e instanceof Down ? {...s,
      frog:{...s.frog, y:fixed(s.frog.y + e.down)}
    } : tick(s, e.elapsed) 

    // main game stream
    const subscription = merge(gameClock,startLeft,startRight,startUp,starttDown).pipe(scan(reduceState,initialState))
    .subscribe(updateView)

}

// The following simply runs your main function on window load.  Make sure to leave it in place.
if (typeof window !== "undefined") {
  window.onload = () => {
    main();
  };
}

