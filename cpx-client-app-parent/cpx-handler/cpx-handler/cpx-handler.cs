using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace cpx_handler
{
    class Program
    {
        static void Main(string[] args)
        {
            //args = new String[] { "cpx:fall_260590322_f484020@dbsys2:DATABASE" }; //-> uncomment and adapt this for debug testing
            //args = new String[] { "C:\\workspace\\CheckpointX_Repository\\trunk\\Männliche Patienten.cpxf" }; //-> uncomment and adapt this for debug testing
            if (args == null || args.Length == 0)
            {
                Console.WriteLine("No arguments passed, cannot send message to CPX Client");
                MessageBox.Show("Der CPX Handler kann nicht als Einzelanwendung gestartet werden, sondern benötigt einen Weiterleitungs-Parameter!\n\nVerknüpfen Sie in Windows bspw. cpxf-Dateien mit dem CPX Handler, um ihn benutzen zu können.", "CPX Handler kann nicht ausgeführt werden", MessageBoxButtons.OK, MessageBoxIcon.Information);
                Environment.Exit(-1);
            }
            string message = args[0].Trim();
            Console.WriteLine("Process message '" + message + "'");
            int retCode = SendMessage(message);
            Environment.Exit(retCode);
        }

        [DllImport("Kernel32.dll", SetLastError = true)]
        [return: MarshalAs(UnmanagedType.U4)]
        public static extern int WTSGetActiveConsoleSessionId();

        protected static int SendMessage(string message)
        {
            //string userName = System.Security.Principal.WindowsIdentity.GetCurrent().Name;
            string userName = Environment.UserName;
            int sessionId = WTSGetActiveConsoleSessionId();
            Console.WriteLine("Current User: " + userName);
            Console.WriteLine("Current Windows Session ID: " + userName);
            int connection_timeout = 10; //ms
            //string message = "DIS IS A GRAET MSG FUR CPX!";
            byte[] msg = Encoding.UTF8.GetBytes(userName + "!@:" + sessionId + "?@:" + message);
            byte[] bytes = new byte[8096];
            int port = 11111;
            int max_port = 11131;
            Socket socket;

            bool successDelivered = false;
            bool errorNoTriesLeft = false;
            bool errorClientRejected = false;

            while (true)
            {
                IPHostEntry ipHostInfo = Dns.GetHostEntry(Dns.GetHostName());
                IPAddress ipAddress = ipHostInfo.AddressList[0];
                //IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

                // Create a TCP/IP  socket.  
                socket = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

                IAsyncResult result = socket.BeginConnect(ipAddress, port, null, null);

                bool success = result.AsyncWaitHandle.WaitOne(connection_timeout, true);

                if (socket.Connected)
                {
                    socket.ReceiveTimeout = 25; //1000 = 1s
                    socket.SendTimeout = 100; //1000 = 1s
                    Console.WriteLine("Found CPX Client on port " + port + "! Will deliver message now...");
                    int byteCount = socket.Send(msg, SocketFlags.None);
                    int bytesRec = socket.Receive(bytes);
                    String resultServer = Encoding.ASCII.GetString(bytes, 0, bytesRec);

                    if (resultServer.Trim().Equals("NOK"))
                    {
                        errorClientRejected = true;
                        Console.WriteLine("Was not able to deliver message '" + message + "', CPX Client on port " + port + " rejected to process it (response was " + resultServer + ")!");
                    } else
                    {
                        successDelivered = true;
                        Console.WriteLine("Message '" + message + "' was successfully delivered to CPX Client on port " + port + " (response was " + resultServer + ")!");
                        break;
                    }
                }
                else
                {
                    Console.WriteLine("No CPX Client is listening to port " + port);
                }

                socket.Close();
                if (port >= max_port)
                {
                    errorNoTriesLeft = true;
                    break;
                }
                port++;

            }

            socket.Close();

            if (successDelivered)
            {
                return 0;
            }
            if (errorClientRejected)
            {
                int atPos = message.LastIndexOf("@");
                if (atPos > -1)
                {
                    string database = message.Substring(atPos + 1).Trim();
                    int colonPos = database.IndexOf(":");
                    if (colonPos > -1)
                    {
                        //Try to send without persistence unit (like dbsys1), but with database name
                        Console.WriteLine("Message '" + message + "' was rejected by at least one client, I will try to send this message again without persistence unit information (like dbsys1)!");
                        string newDatabase = database.Substring(colonPos + 1).Trim();
                        message = message.Substring(0, atPos).Trim();
                        message += "@" + newDatabase;
                        return SendMessage(message);
                    }
                    else
                    {
                        //Try to send without database information (no persistence unit like dbsys1 and no database name)
                        Console.WriteLine("Message '" + message + "' was rejected by at least one client, I will try to send this message again without any target database information (no persistence unit like dbsys1 and no database name)!");
                        message = message.Substring(0, atPos).Trim();
                        return SendMessage(message);
                    }
                }
                else
                {
                    MessageBox.Show("Die Anfrage " + message + " konnte nicht verarbeitet werden.\n\nDer CPX Client konnte die Anfrage nicht verarbeiten. Bitte prüfen Sie, ob sie auf der korrekten CPX-Datenbank angemeldet sind!", "CPX Client hat die Anfrage zurückgewiesen", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    return -2;
                }
            }
            if (errorNoTriesLeft)
            {
                MessageBox.Show("Die Anfrage " + message + " konnte nicht verarbeitet werden.\n\nStarten Sie zunächst CPX und melden Sie sich an der gewünschten Datenbank an", "CPX Client nicht gefunden", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return -3;
            }
            //return code -4 should never happen!
            return -4;
        }
    }
}
