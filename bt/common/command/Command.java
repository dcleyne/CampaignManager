package bt.common.command;

public enum Command 
{
    //Global Server Commands	
    MessageStart,
    MessageEnd,    
	
	Disconnect,
    Error,
    Version,
    ServerStatus,
    ServerShutdown,
    PlayerInfo,
    PlayerDisconnected,

    ClientConnected,
    
    ConnectionType,
    ServerAdminPassword,
    ServerPassword,
    Handshake,

    GameAdministrator,
    GameAdministratorDisconnected,
    CurrentParticipants,
    ParticipantAdded,
    ParticipantRemoved,
    
    SolarSystemDetails,
    UnitList,
    PlayerUnitList,
    UnitDetails,

    PlayerList,
    PlayerDetails,

    OrderQueueOpen,
    OrderQueueClosed;

}
