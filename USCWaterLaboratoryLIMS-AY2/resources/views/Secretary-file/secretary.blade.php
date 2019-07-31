@extends('layouts.secretary_app')

@section('content')
<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-10">
            <div class="card">
                <div class="card-header">
                    Notifications
                    {{-- modal trigger --}}
                    <button type="button" class="btn btn-primary btn-sm float-right" data-toggle="modal" data-target="#markAllAsRead">
                        Mark all as read <i class="fa fa-check"></i>
                    </button>
                    {{-- modal --}}
                    <div class="modal fade" id="markAllAsRead" tabindex="-1" role="dialog" aria-labelledby="markAllAsReadLabel" aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content bg-primary">
                                <div class="modal-header">
                                    <h5 class="modal-title text-white" id="markAllAsReadLabel">Read All Notifications</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span class="text-white" aria-hidden="true">&times;</span></button>
                                </div>
                                <div class="modal-body bg-white">
                                    Are you sure you want to mark all notifications as read?
                                </div>
                                <div class="modal-footer bg-white">
                                    <form action="{{route('read-all')}}" method="post">
                                        @csrf
                                        <button type="submit" class="btn btn-primary">Yes</button>
                                    </form>
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                    @if ($user->notifications()->count() == 0)
                        <div class="alert alert-info m-0" role="alert">
                            <p>There are no notifications to be read.</p>
                        </div>
                    @endif
            
                    @foreach ($user->notifications()->paginate(10) as $notification)
                    {{-- @php
                        dd($notification)
                    @endphp --}}
                        <div @if($notification->read_at == NULL && $notification->data['days'] == 0) class="alert alert-danger m-1" @elseif($notification->read_at != NULL) class="alert alert-secondary m-1" @else class="alert alert-info m-1" @endif role="alert">
                            <div class="row">
                                <div class="col-md-8">
                                    <h5 class="alert-heading">
                                        {{ $notification->data['message'].'.' }}
                                    </h5>
                                    <p class="mb-0">
                                        Laboratory Code: <strong>{{ $notification->data['labCode'] }}</strong> <br>
                                        Created by: 
                                            @if (isset($notification->data['created_by']))
                                                {{ $notification->data['created_by'] }}
                                            @else 
                                                NULL
                                            @endif
                                        <br>
                                        Due Date: {{ date("F d, Y h:i A", strtotime($notification->data['dueDate'])) }}
                                    </p>                                    
                                </div>
                                <div class="col-md-4">
                                    @if($notification->read_at != NULL)
                                        <button type="submit" class="float-right mb-0 btn btn-secondary btn-sm" disabled>Mark as read</button>
                                    @else
                                        <form action="{{route('notif-read', ['id' => $notification->id])}}" method="get">
                                            @csrf
                                            <button type="submit" class="float-right mb-0 btn btn-secondary btn-sm">Mark as read</button>
                                        </form>
                                    @endif
                                </div>
                            </div>
                            
                        </div>
                    @endforeach
    
                </div>
            </div>
        </div>
    </div>
    <div class="row justify-content-center m-2">
        {{ $user->unreadNotifications()->paginate(10)->links() }}
    </div>
</div>
@endsection