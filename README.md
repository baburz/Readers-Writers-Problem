# Readers-Writers-Problem
Reader-Writers problem can be defined as several processes trying to access a shared variable. To avoid conflictions, a mechanism that enables writers to be run sequential and readers to be parallel should be defined.
Project Requirements 
If a writer has begun the writing process, then
•	No additional writer can perform the write function 
•	No reader is allowed to read 
If one or more readers are reading, then 
•	Other readers may read as well 
•	No writer may perform write function until all readers have finished reading  

#Solution
I used 3 semaphores: enter, enter2, and writer.
Also, I have 3 variables:
•	s_reader, which counts readers that started reading, 
•	c_reader, which counts readers that completed reading 
•	boolean w_writer that keeps if there is any waiting writing operation.

#Writer
When a process that performs a write operation is run, semaphores enter and enter2 will go on wait state and the algorithm will check if all the read operations are done. 
•	If it is done, semaphore enter2 will be signaled, and write operation will be performed. Because enter is in the wait state, no other thread can be run at that time.
•	If it is not done, w_writer will be turned to true. Semaphore enter2 will be signaled and the semaphore writer will go on wait state. Writer will wait until reading operations are all done. Then variable w_writer will be turned into false since program won’t be wating anymore. Write operation will be performed.
After those, semaphore enter will be signaled.

#Reader(read)
When a read operation is run, the s_reader variable is going to be incremented in the critical section of semaphore enter, and then the reader will read the data. Since the reading operation should be performed parallel, it does not have to be in the critical section of any semaphore. Also writer cannot write at that time because if any reader is running, s_reader will never be equal to c_reader. After the reading is completed, semaphore enter2 will go on wait state and c_reader will be incremented. Then the program will check if all the reading operations are done and any writer is waiting. If both conditions are provided, the semaphore writer will be signaled so that a writing operation can be performed. After that, semaphore enter2 will be signaled.
