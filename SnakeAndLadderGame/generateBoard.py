import random
def check(n):
    if n>=10 and n%10==0:
        return True
    return False

def printBoard(board):
    print("\n")
    n=len(board)
    for i in range(n):
        print(board[i],end="\t")
        if (i+1)%10==0:
            print("\n")
    print("\n")
def generateBoard(n,optMoves,board):
    for i in range(len(optMoves)):
        move=int(optMoves[i])
        board[move-1]="At Here"
        #print("iteration :- ",i)
        #printBoard(board)
        #printBoard(n,optMoves,board)
    board[n-1]="At Here"
    return board
def possiblePositions(n):
    flag=True
    while flag:
        pos1 = random.randint(0,n-1)
        pos2 = random.randint(0,n-1)
        if pos1>pos2:
            return [pos1,pos2]
    
def takeSnakes(numSnakes,n):
    ans=[]
    for i in range(numSnakes):
        ans.append(possiblePositions(n))
    return ans

def checkAllPos(snakesPos,laddersPos):
    for a in snakesPos:
        for b in laddersPos:
            if a[0]==b[0] or a[0]==b[1] or a[1]==b[0] or a[1]==b[1] or a[0]==a[1] or b[0]==b[1]:
                return False
    for i in range(len(snakesPos)):
        for j in range(i+1,len(snakesPos)):
            a,b = snakesPos[i],snakesPos[j]
            if a[0]==b[0] or a[0]==b[1] or a[1]==b[0] or a[1]==b[1] or a[0]==a[1] or b[0]==b[1]:return False

    for i in range(len(laddersPos)):
        for j in range(i+1,len(laddersPos)):
            a,b = laddersPos[i],laddersPos[j]
            if a[0]==b[0] or a[0]==b[1] or a[1]==b[0] or a[1]==b[1] or a[0]==a[1] or b[0]==b[1]:return False
    return True

def takeSnakesAndLadders(numSnakes,numLadders,n):
    flag=True
    while flag:
        snakesPos = takeSnakes(numSnakes,n)
        laddersPos = takeSnakes(numLadders,n)
        if checkAllPos(snakesPos,laddersPos):
            return (snakesPos,laddersPos)
        
def solve(n,numSnakes,numLadders):
    # the target square and the positions of the snakes and ladders:
    top = n
    jump = {}
    snakesPos,laddersPos = takeSnakesAndLadders(numSnakes,numLadders,n)
    board=["-" for i in range(1,n+1)]
    #print("board",board)
    #printBoard(board)
    #snakesPos=takeSnakes(numSnakes,n)
    #print("snakesPos",snakesPos)
    #laddersPos=takeSnakes(numLadders,n)
    #print("laddersPos",laddersPos)
    i,j=1,1
    for [a,b] in laddersPos:
        jump[b]=a
        board[b]='L'+str(i)
        board[a]='L'+str(i)
        i+=1
    for [a,b] in snakesPos:
        jump[a]=b
        board[b]='S'+str(j)
        board[a]='S'+str(j)
        j+=1
    #print("jump",jump)
    # start from square 0 (= outside the board) after 0 rolls
    open = {0}
    path = {0: ()}

    while len(open) > 0:
        i = open.pop()
        p = path[i] + (i,)
        for j in range(i+1, i+7):
            if j > top: break
            if j in jump: j = jump[j]
            if j not in path or len(path[j]) > len(p):
                open.add(j)
                path[j] = p
    #print("board",board)
    #printBoard(board)
    #for i in path:
    #    print("Square", i, "can be reached in", len(path[i]), "rolls via", path[i])
    optMoves = [str(i+1) for i in path[n]]
    board=generateBoard(n,optMoves,board)
    #printBoard(board)
    #print("optMoves",optMoves)
    return board
    
n=int(input("Enter final position of board:-\n"))
numSnakes=2
numLadders=3
errorMsg="Enter valid number which is >= 10 and divisible by 10 like 10,20,30... "
       
if check(n):
    finalBoard = solve(n,numSnakes,numLadders)
    printBoard(finalBoard)
else:
    print(errorMsg)
